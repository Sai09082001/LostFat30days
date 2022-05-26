package com.lubuteam.sellsource.losefat.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

import com.ads.control.funtion.UtilsApp;
import com.lubuteam.sellsource.losefat.BuildConfig;
import com.lubuteam.sellsource.losefat.R;
import com.lubuteam.sellsource.losefat.data.model.ContainerVoiceEngine;
import com.lubuteam.sellsource.losefat.data.model.MessageEvent;
import com.lubuteam.sellsource.losefat.data.repositories.DailySectionRepository;
import com.lubuteam.sellsource.losefat.data.repositories.DayHistoryRepository;
import com.lubuteam.sellsource.losefat.data.repositories.ReminderRepository;
import com.lubuteam.sellsource.losefat.data.repositories.SectionHistoryRepository;
import com.lubuteam.sellsource.losefat.data.repositories.SectionRepository;
import com.lubuteam.sellsource.losefat.data.repositories.WorkoutRepository;
import com.lubuteam.sellsource.losefat.data.shared.AppSettings;
import com.lubuteam.sellsource.losefat.ui.activities.ProfileActivity;
import com.lubuteam.sellsource.losefat.ui.activities.ReminderActivity;
import com.lubuteam.sellsource.losefat.ui.activities.SplashActivity;
import com.lubuteam.sellsource.losefat.ui.base.BaseActivity;
import com.lubuteam.sellsource.losefat.ui.base.BaseFragment;
import com.lubuteam.sellsource.losefat.ui.dialogs.RestSetDialog;
import com.lubuteam.sellsource.losefat.ui.interfaces.DialogResultListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;



public class SettingsFragment extends BaseFragment implements DialogResultListener, CompoundButton.OnCheckedChangeListener {

    private TextToSpeech tts;
    private boolean ready = false;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        initViews();
        initEvents();
        initObservers();
        return rootView;
    }

    private void initTTS(boolean isTest) {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            ready = false;
        }
        tts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    String language = AppSettings.getInstance().getEngineLanguage();
                    Locale locale = Locale.ENGLISH;
                    if (!language.isEmpty()) {
                        locale = ContainerVoiceEngine.toLocal(language);
                    }
                    int result = tts.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getContext(), getResources().getString(R.string.this_language_not_support),
                                Toast.LENGTH_SHORT).show();
                        AppSettings.getInstance().setEngineLanguage("");
                        refreshEngineLanguage();
                        initTTS(false);
                    } else {
                        ready = true;
                        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onStart(String s) {
//                                Log.e("status", "start");
                            }

                            @SuppressLint("CheckResult")
                            @Override
                            public void onDone(String s) {
//                                Log.e("status", "done");
                                Flowable.just(0)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(response -> showAskDialog(), Throwable::printStackTrace);
                            }

                            @Override
                            public void onError(String s) {
                                Toast.makeText(getContext(), s,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        tts.setPitch(0.9f);
                        tts.setSpeechRate(1.0f);
                        if (isTest) {
                            speech(getResources().getString(R.string.did_you_hear_the_test_voice));
                        }
                    }
                }
            }
        }, AppSettings.getInstance().getEngineDefault().getPackageName());
    }

    private void speech(String text) {
        if (tts != null && ready) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        if (AppSettings.getInstance().getEngineDefault() == null) {
            getEngines();
        } else {
            initTTS(false);
        }
        refreshAllView();
    }

    @Override
    protected void initEvents() {
        super.initEvents();
        addSoundListener();
        rootView.findViewById(R.id.row_profile).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        });
        rootView.findViewById(R.id.row_reminder).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ReminderActivity.class);
            startActivity(intent);
        });
        rootView.findViewById(R.id.row_rest_set).setOnClickListener(view -> {
            new RestSetDialog(this, true).show(getChildFragmentManager(), null);
        });
        rootView.findViewById(R.id.row_countdown).setOnClickListener(view -> {
            new RestSetDialog(this, false).show(getChildFragmentManager(), null);
        });
        rootView.findViewById(R.id.row_device_setting).setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction("com.android.settings.TTS_SETTINGS");
            startActivity(intent);
        });
        rootView.findViewById(R.id.row_download_engine).setOnClickListener(view -> {
            downloadEngine();
        });
        rootView.findViewById(R.id.row_download_more).setOnClickListener(view -> {
            try {
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        rootView.findViewById(R.id.row_test_voice).setOnClickListener(view -> {
            speech(getResources().getString(R.string.did_you_hear_the_test_voice));
        });
        rootView.findViewById(R.id.row_engine).setOnClickListener(view -> {
            getEngines();
        });
        rootView.findViewById(R.id.row_voice_language).setOnClickListener(view -> {
            showEngineLanguageDialog();
        });
        rootView.findViewById(R.id.row_language).setOnClickListener(view -> {
            showLanguageDialog();
        });
        rootView.findViewById(R.id.row_gender).setOnClickListener(view -> {
            showGenderDialog();
        });
        rootView.findViewById(R.id.row_restart_progress).setOnClickListener(view -> {
            showRestartProgressDialog();
        });
        rootView.findViewById(R.id.row_delete_all_data).setOnClickListener(view -> {
            showDeleteAllDataDialog();
        });

        rootView.findViewById(R.id.row_share).setOnClickListener(view -> {
            UtilsApp.shareApp(getActivity());
        });
        rootView.findViewById(R.id.row_rating).setOnClickListener(view -> {
            UtilsApp.RateApp(getActivity());
        });
        rootView.findViewById(R.id.row_feed_back).setOnClickListener(view -> {
            UtilsApp.SendFeedBack(getActivity(),getString(R.string.email_feedback),getString(R.string.Title_email));
        });
        rootView.findViewById(R.id.row_help).setOnClickListener(view -> {
            UtilsApp.SendFeedBack(getActivity(),getString(R.string.email_feedback),"Workout for women feedback");
        });
        rootView.findViewById(R.id.row_policy).setOnClickListener(view -> {
            UtilsApp.OpenBrower(getActivity(),getString(R.string.link_policy));
        });
    }

    @SuppressLint("CheckResult")
    private void showDeleteAllDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.title_delete_all_data_dialog));
        builder.setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> {
            // Restart progress daily
            dialogInterface.dismiss();
            DailySectionRepository.getInstance().resetAll();
            DayHistoryRepository.getInstance().deleteAll().subscribe(() -> {
                ReminderRepository.getInstance().deleteAll().subscribe(() -> {
                    SectionHistoryRepository.getInstance().deleteAll().subscribe(() -> {
                        SectionRepository.getInstance().deleteAllTraining();
                        SectionRepository.getInstance().resetAll();
                        WorkoutRepository.getInstance().resetAll();
                        AppSettings.getInstance().clearAll();
                        refreshAllView();
                        AppSettings.getInstance().markedFirstOpen();
                        AppSettings.getInstance().setLastVersion(BuildConfig.VERSION_CODE);
                        Intent intent = new Intent(getContext(), SplashActivity.class);
                        getActivity().finishAffinity();
                        getActivity().startActivity(intent);
                    });
                });
            });
        });
        builder.setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    private void showRestartProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.title_restart_progress_dialog));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Restart progress daily
                DailySectionRepository.getInstance().resetAll();
                Intent intent = new Intent(getContext(), SplashActivity.class);
                getActivity().finishAffinity();
                getActivity().startActivity(intent);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    private void addSoundListener() {
        ((SwitchCompat) rootView.findViewById(R.id.sw_mute)).setOnCheckedChangeListener(this);
        ((SwitchCompat) rootView.findViewById(R.id.sw_voice)).setOnCheckedChangeListener(this);
        ((SwitchCompat) rootView.findViewById(R.id.sw_coach)).setOnCheckedChangeListener(this);
    }

    private void removeSoundListener() {
        ((SwitchCompat) rootView.findViewById(R.id.sw_mute)).setOnCheckedChangeListener(null);
        ((SwitchCompat) rootView.findViewById(R.id.sw_voice)).setOnCheckedChangeListener(null);
        ((SwitchCompat) rootView.findViewById(R.id.sw_coach)).setOnCheckedChangeListener(null);
    }

    private void refreshSound() {
        ((SwitchCompat) rootView.findViewById(R.id.sw_mute)).setChecked(!AppSettings.getInstance().getSound());
        ((SwitchCompat) rootView.findViewById(R.id.sw_voice)).setChecked(AppSettings.getInstance().getSoundVoice());
        ((SwitchCompat) rootView.findViewById(R.id.sw_coach)).setChecked(AppSettings.getInstance().getSoundCoachTips());
    }

    private void downloadEngine() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=text%20to%20speech&c=apps")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ContainerVoiceEngine> containerVEArray;
    private int requestCount;

    private void getEngines() {
        requestCount = 0;

        final Intent ttsIntent = new Intent();
        ttsIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        final PackageManager pm = getContext().getPackageManager();
        final List<ResolveInfo> list = pm.queryIntentActivities(ttsIntent, PackageManager.GET_META_DATA);

        containerVEArray = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {

            final ContainerVoiceEngine cve = new ContainerVoiceEngine();

            cve.setLabel(list.get(i).loadLabel(pm).toString());
            cve.setPackageName(list.get(i).activityInfo.applicationInfo.packageName);

            final Intent getIntent = new Intent();
            getIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);

            getIntent.setPackage(cve.getPackageName());
            getIntent.getStringArrayListExtra(TextToSpeech.Engine.EXTRA_AVAILABLE_VOICES);
            getIntent.getStringArrayListExtra(TextToSpeech.Engine.EXTRA_UNAVAILABLE_VOICES);
            cve.setIntent(getIntent);
            containerVEArray.add(cve);
        }

//        Log.e("status", "containerVEArray: " + containerVEArray.size());

        for (int i = 0; i < containerVEArray.size(); i++) {
            startActivityForResult(containerVEArray.get(i).getIntent(), i);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("status", "onActivityResult: requestCount: " + " - requestCode: " + requestCode);
        requestCount++;

        try {
            if (data != null) {
                final Bundle bundle = data.getExtras();
                if (bundle != null) {
//                    Log.e("status", containerVEArray.get(requestCode).getLabel() + " - Bundle Data");

                    final Set<String> keys = bundle.keySet();
                    final Iterator<String> it = keys.iterator();

                    while (it.hasNext()) {
                        final String key = it.next();
//                        Log.e("status", "Key: " + key + " = " + bundle.get(key));
                    }
                }

                if (data.hasExtra("availableVoices")) {
                    containerVEArray.get(requestCode).setVoices(data.getStringArrayListExtra("availableVoices"));
                } else {
                    containerVEArray.get(requestCode).setVoices(new ArrayList<>());
                }
            }

            if (requestCount == containerVEArray.size()) {
                for (int i = 0; i < containerVEArray.size(); i++) {
//                    Log.e("status", "cve: " + containerVEArray.get(i).getLabel() + " - "
//                            + containerVEArray.get(i).getVoices().size() + " - " + containerVEArray.get(i).getVoices().toString());
//                    Log.e("status", ContainerVoiceEngine.format(containerVEArray.get(i).getVoices().get(0)));
                }

                // Check already engines. If not -> save first and refresh view
                if (AppSettings.getInstance().getEngineDefault() == null && containerVEArray.size() > 0) {
                    AppSettings.getInstance().setEngineDefault(containerVEArray.get(0));
                    AppSettings.getInstance().setEngineLanguage("eng-USA");
                    refreshEngine();
                    refreshEngineLanguage();
                    initTTS(false);
                    return;
                }

                // Finish all request -> show dialog engine
                showEngineSelectDialog();
            }
        } catch (final IndexOutOfBoundsException e) {
//            Log.e("status", "IndexOutOfBoundsException");
            e.printStackTrace();
        } catch (final NullPointerException e) {
//            Log.e("status", "NullPointerException");
            e.printStackTrace();
        } catch (final Exception e) {
//            Log.e("status", "Exception");
            e.printStackTrace();
        }
    }

    private void showLanguageDialog() {
        String[] languagesDisplay = getResources().getStringArray(R.array.language_display);
        String[] languagesCode = getResources().getStringArray(R.array.language_value);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.title_languages_dialog));
        builder.setItems(languagesDisplay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                String code = languagesCode[i];
                AppSettings.getInstance().setLanguage(code);
                ((BaseActivity) getActivity()).setLocale(code);
            }
        });
        builder.create().show();
    }

    private void showGenderDialog() {
        ArrayList<String> genders = new ArrayList<>();
        genders.add(getString(R.string.female));
        genders.add(getString(R.string.male));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.title_gender_dialog));
        builder.setItems(genders.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                AppSettings.getInstance().setGender(i);
                refreshGender();
                EventBus.getDefault().post(new MessageEvent(MessageEvent.CHANGE_GENDER, null));
            }
        });
        builder.create().show();
    }

    private void showEngineSelectDialog() {
        ArrayList<String> engines = new ArrayList<>();
        for (ContainerVoiceEngine engine : containerVEArray) {
            engines.add(engine.getLabel());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.please_choose_voide_engine));
        builder.setSingleChoiceItems(engines.toArray(new String[0]), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                AppSettings.getInstance().setEngineDefault(containerVEArray.get(i));
                AppSettings.getInstance().setEngineLanguage("");
                refreshEngine();
                refreshEngineLanguage();
                initTTS(true);
            }
        });
        builder.create().show();
    }

    private void showEngineLanguageDialog() {
        final ContainerVoiceEngine engine = AppSettings.getInstance().getEngineDefault();
        if (engine == null) return;
        ArrayList<String> voices = engine.getVoices();
        Collections.sort(voices);
        ArrayList<String> showList = new ArrayList<>();
        for (String language : voices) {
//            Log.e("status", language);
            Locale locale = ContainerVoiceEngine.toLocal(language);
            showList.add(locale.getDisplayLanguage() + " - " + locale.getDisplayCountry());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.please_choose_language_voice_engine));
        builder.setSingleChoiceItems(showList.toArray(new String[0]), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Log.e("status", "click: " + i + " - " + engine.getVoices().get(i));
                dialogInterface.dismiss();
                AppSettings.getInstance().setEngineLanguage(engine.getVoices().get(i));
                refreshEngineLanguage();
                initTTS(true);
            }
        });
        builder.create().show();
    }

    private void showAskDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage(getResources().getString(R.string.did_you_hear_the_test_voice))
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        showWarmingDialog();
                    }
                })
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void showWarmingDialog() {
        View download, select;
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_warming_test_voice);
        download = dialog.findViewById(R.id.btn_download_tts_engine);
        select = dialog.findViewById(R.id.btn_select_tts_engine);
        download.setOnClickListener(view -> {
            dialog.dismiss();
            downloadEngine();
        });
        select.setOnClickListener(view -> {
            dialog.dismiss();
            getEngines();
        });
        dialog.show();
    }

    @Override
    public void onResult(int type, Object value) {
        switch (type) {
            case DialogResultListener.COUNTDOWN:
            case DialogResultListener.REST_SET:
                refreshDuration();
                break;
        }
    }

    private void refreshAllView() {
        refreshDuration();
        refreshEngine();
        refreshEngineLanguage();
        refreshLanguage();
        refreshSound();
        refreshGender();
    }

    private void refreshLanguage() {
        String[] languagesCode = getResources().getStringArray(R.array.language_value);
        String[] languagesDisplay = getResources().getStringArray(R.array.language_display);
        String current = AppSettings.getInstance().getLanguage();
        int index = Arrays.asList(languagesCode).indexOf(current);
        ((TextView) rootView.findViewById(R.id.txt_language_value)).setText(languagesDisplay[index]);
    }

    private void refreshDuration() {
        ((TextView) rootView.findViewById(R.id.txt_rest_set_value)).setText(AppSettings.getInstance().getRestSet() + " secs");
        ((TextView) rootView.findViewById(R.id.txt_countdown_value)).setText(AppSettings.getInstance().getCountDown() + " secs");
    }

    private void refreshGender() {
        String gender = AppSettings.getInstance().getGender() == 0 ?
                getResources().getString(R.string.female)
                : getResources().getString(R.string.male);
        ((TextView) rootView.findViewById(R.id.txt_gender_value)).setText(gender);
    }

    private void refreshEngine() {
        ContainerVoiceEngine engine = AppSettings.getInstance().getEngineDefault();
        ((TextView) rootView.findViewById(R.id.txt_engine_value)).setText(engine == null ? "" : engine.getLabel().split(" ")[0]);
    }

    private void refreshEngineLanguage() {
        try {
            String language = AppSettings.getInstance().getEngineLanguage();
            Locale locale = Locale.getDefault();
            if (!language.isEmpty()) {
                locale = ContainerVoiceEngine.toLocal(language);
            }
            ((TextView) rootView.findViewById(R.id.txt_voice_language_value))
                    .setText(language.isEmpty() ? getResources().getString(R.string.default_string) : locale.getDisplayLanguage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.sw_mute:
                AppSettings.getInstance().setSound(!b);
                removeSoundListener();
                if (b) {
                    ((SwitchCompat) rootView.findViewById(R.id.sw_voice)).setChecked(false);
                    ((SwitchCompat) rootView.findViewById(R.id.sw_coach)).setChecked(false);
                    rootView.findViewById(R.id.sw_voice).setEnabled(false);
                    rootView.findViewById(R.id.sw_coach).setEnabled(false);
                } else {
                    ((SwitchCompat) rootView.findViewById(R.id.sw_voice)).setChecked(AppSettings.getInstance().getSoundVoice());
                    ((SwitchCompat) rootView.findViewById(R.id.sw_coach)).setChecked(AppSettings.getInstance().getSoundCoachTips());
                    rootView.findViewById(R.id.sw_voice).setEnabled(true);
                    rootView.findViewById(R.id.sw_coach).setEnabled(true);
                }
                addSoundListener();
                break;
            case R.id.sw_voice:
                AppSettings.getInstance().setSoundVoice(b);
                break;
            case R.id.sw_coach:
                AppSettings.getInstance().setSoundCoachTips(b);
                break;
        }
    }
}
