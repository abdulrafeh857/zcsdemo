package com.zcs.zcssdkdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.zcs.sdk.DriverManager;
import com.zcs.sdk.SdkData;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.card.CardInfoEntity;
import com.zcs.sdk.card.CardReaderManager;
import com.zcs.sdk.card.CardReaderTypeEnum;
import com.zcs.sdk.card.CardSlotNoEnum;
import com.zcs.sdk.card.ICCard;
import com.zcs.sdk.card.MagCard;
import com.zcs.sdk.card.RfCard;
import com.zcs.sdk.listener.OnSearchCardListener;
import com.zcs.sdk.util.StringUtils;
import com.zcs.zcssdkdemo.utils.DialogUtils;

import java.util.Arrays;

public class CardFragment extends PreferenceFragment {

    private static final String TAG = "CardFragment";

    private Context mContext;

    private static final String KEY_IC_CARD = "IC_card_key";
    private static final String KEY_PSAM_CARD = "psam_card_key";
    private static final String KEY_MAGNETIC_CARD = "magnetic_card_key";
    private static final String KEY_CONTACTLESS_CARD = "contactless_card_key";
    private static final String KEY_FELICA_CARD = "felica_card_key";
    private static final String KEY_READ_M1_CARD = "read_M1_card_key";
    private static final String KEY_WRITE_M1_CARD = "write_M1_card_key";
    private static final String KEY_READ_MIFAREPLUS_CARD = "read_Mifare_Plus_card_key";
    private static final String KEY_NTAG = "ntag_key";
    private static final String KEY_SLE4442 = "sle4442_key";
    private static final String KEY_SLE4428 = "sle4428_key";
    private static final String KEY_AT24 = "at24_key";
    private static final String KEY_AT102 = "at102_key";
    private static final String KEY_AT1608 = "at1608_key";
    private static final String KEY_AT153 = "at153_key";
    private static final String KEY_EXTERNAL_IC = "external_ic_key";
    private static final String KEY_EMV = "emv_key";
    private static final String KEY_THAILAND_CARD = "thailand_card_key";

    private static final int READ_TIMEOUT = 60 * 1000;

    private ProgressDialog mProgressDialog;

    private DriverManager mDriverManager;
    private CardReaderManager mCardReadManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        addPreferencesFromResource(R.xml.pref_card);
        mDriverManager = DriverManager.getInstance();
        mCardReadManager = mDriverManager.getCardReadManager();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findPreference(KEY_IC_CARD).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                searchICCard();
                return true;
            }
        });

        findPreference(KEY_PSAM_CARD).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                searchPSAM1();
                return true;
            }
        });

        findPreference(KEY_MAGNETIC_CARD).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                searchMagnetCard();
                return true;
            }
        });

        findPreference(KEY_CONTACTLESS_CARD).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                searchRfCard();
                return true;
            }
        });

        findPreference(KEY_FELICA_CARD).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                searchFelicaCard();
                return true;
            }
        });

        findPreference(KEY_READ_M1_CARD).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showReadM1Dialog(mContext);
                return true;
            }
        });

        findPreference(KEY_WRITE_M1_CARD).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showWriteM1Dialog(mContext);
                return true;
            }
        });

        findPreference(KEY_READ_MIFAREPLUS_CARD).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showReadMifarePlusDialog(mContext);
                return true;
            }
        });

        findPreference(KEY_NTAG).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), Ntagctivity.class));
                return true;
            }
        });

        findPreference(KEY_SLE4442).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), SLE4442Activity.class));
                return true;
            }
        });

        findPreference(KEY_SLE4428).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), SLE4428Activity.class));
                return true;
            }
        });

        findPreference(KEY_AT24).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), AT24CXXActivity.class));
                return true;
            }
        });

        findPreference(KEY_AT102).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), AT102Activity.class));
                return true;
            }
        });

        findPreference(KEY_AT1608).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), AT1608Activity.class));
                return true;
            }
        });

        findPreference(KEY_AT153).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), AT153Activity.class));
                return true;
            }
        });

        findPreference(KEY_EXTERNAL_IC).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), ExternalPortActivity.class));
                return true;
            }
        });

        findPreference(KEY_EMV).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), EmvActivity.class));
                return true;
            }
        });


        findPreference(KEY_THAILAND_CARD).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                readThailandCard();
                return true;
            }
        });
    }

    private void searchICCard() {
        showSearchCardDialog(R.string.waiting, R.string.msg_ic_card);
        mCardReadManager.cancelSearchCard();
        mCardReadManager.searchCard(CardReaderTypeEnum.IC_CARD, READ_TIMEOUT, mICCardSearchCardListener);
    }

    private OnSearchCardListener mICCardSearchCardListener = new OnSearchCardListener() {
        @Override
        public void onCardInfo(CardInfoEntity cardInfoEntity) {
            mProgressDialog.dismiss();
            readICCard();
        }

        @Override
        public void onError(int i) {
            mProgressDialog.dismiss();
            showReadICCardErrorDialog(i);
        }

        @Override
        public void onNoCard(CardReaderTypeEnum cardReaderTypeEnum, boolean b) {

        }
    };

    public static final byte[] APDU_SEND_IC = {0x00, (byte) 0xA4, 0x04, 0x00, 0x0E, 0x31, 0x50, 0x41, 0x59, 0x2E, 0x53, 0x59, 0x53, 0x2E, 0x44, 0x44, 0x46, 0x30, 0x31, 0X00};
    private void readICCard() {
        ICCard icCard = mCardReadManager.getICCard();
        int result = icCard.icCardReset(CardSlotNoEnum.SDK_ICC_USERCARD);
        if (result == SdkResult.SDK_OK) {
            int[] recvLen = new int[1];
            byte[] recvData = new byte[300];
            result = icCard.icExchangeAPDU(CardSlotNoEnum.SDK_ICC_USERCARD, APDU_SEND_IC, recvData, recvLen);
            if (result == SdkResult.SDK_OK) {
                final String apduRecv = StringUtils.convertBytesToHex(recvData).substring(0, recvLen[0] * 2);
                CardFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.show(getActivity(), "Read IC card result", apduRecv);
                    }
                });
            } else {
                showReadICCardErrorDialog(result);
            }
        } else {
            showReadICCardErrorDialog(result);
        }
        icCard.icCardPowerDown(CardSlotNoEnum.SDK_ICC_USERCARD);
    }

    private void showReadICCardErrorDialog(final int errorCode) {
        CardFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtils.show(getActivity(), "Read IC card failed", "Error code = " + errorCode);
            }
        });
    }

    private void searchPSAM1() {
        showSearchCardDialog(R.string.waiting, R.string.msg_psam_card);
        mCardReadManager.cancelSearchCard();
        mCardReadManager.searchCard(CardReaderTypeEnum.PSIM1, READ_TIMEOUT, mPSAM1SearchCardListener);
    }

    private OnSearchCardListener mPSAM1SearchCardListener = new OnSearchCardListener() {
        @Override
        public void onCardInfo(CardInfoEntity cardInfoEntity) {
            mProgressDialog.dismiss();
            readPSAM1();
        }

        @Override
        public void onError(int i) {
            mProgressDialog.dismiss();
            showReadPSAM1ErrorDialog(i);
        }

        @Override
        public void onNoCard(CardReaderTypeEnum cardReaderTypeEnum, boolean b) {

        }
    };

    public static final byte[] APDU_SEND_RANDOM = {0x00, (byte) 0x84, 0x00, 0x00, 0x08};
    private void readPSAM1() {
        ICCard icCard = mCardReadManager.getICCard();
        int result = icCard.icCardReset(CardSlotNoEnum.SDK_ICC_SAM1);
        if (result == SdkResult.SDK_OK) {
            int[] recvLen = new int[1];
            byte[] recvData = new byte[300];
            result = icCard.icExchangeAPDU(CardSlotNoEnum.SDK_ICC_SAM1, APDU_SEND_RANDOM, recvData, recvLen);
            if (result == SdkResult.SDK_OK) {
                final String apduRecv = StringUtils.convertBytesToHex(recvData).substring(0, recvLen[0] * 2);
                CardFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.show(getActivity(), "Read PSAM1 result", apduRecv);
                    }
                });
            } else {
                showReadPSAM1ErrorDialog(result);
            }
        } else {
            showReadPSAM1ErrorDialog(result);
        }
        icCard.icCardPowerDown(CardSlotNoEnum.SDK_ICC_SAM1);
    }

    private void showReadPSAM1ErrorDialog(final int errorCode) {
        CardFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtils.show(getActivity(), "Read PSAM1 failed", "Error code = " + errorCode);
            }
        });
    }

    private void searchMagnetCard() {
        showSearchCardDialog(R.string.waiting, R.string.msg_magnet_card);
        mCardReadManager.cancelSearchCard();
        mCardReadManager.searchCard(CardReaderTypeEnum.MAG_CARD, READ_TIMEOUT, mMagnetCardSearchCardListener);
    }

    private OnSearchCardListener mMagnetCardSearchCardListener = new OnSearchCardListener() {
        @Override
        public void onCardInfo(CardInfoEntity cardInfoEntity) {
            mProgressDialog.dismiss();
            readMagnetCard();
        }

        @Override
        public void onError(int i) {
            mProgressDialog.dismiss();
            showReadMagnetCardErrorDialog(i);
        }

        @Override
        public void onNoCard(CardReaderTypeEnum cardReaderTypeEnum, boolean b) {

        }
    };

    private void readMagnetCard() {
        MagCard magCard = mCardReadManager.getMAGCard();
        final CardInfoEntity cardInfoEntity = magCard.getMagReadData();
        if (cardInfoEntity.getResultcode() == SdkResult.SDK_OK) {
            CardFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogUtils.show(getActivity(), "Read magnetic card result", cardInfoToString(cardInfoEntity));
                }
            });
        } else {
            showReadMagnetCardErrorDialog(cardInfoEntity.getResultcode());
        }
        magCard.magCardClose();
    }

    private void showReadMagnetCardErrorDialog(final int errorCode) {
        CardFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtils.show(getActivity(), "Read magnetic card failed", "Error code = " + errorCode);
            }
        });
    }

    private void searchRfCard() {
        showSearchCardDialog(R.string.waiting, R.string.msg_contactless_card);
        mCardReadManager.cancelSearchCard();
        mCardReadManager.searchCard(CardReaderTypeEnum.RF_CARD, READ_TIMEOUT, (byte) (SdkData.RF_TYPE_A | SdkData.RF_TYPE_B), mRfCardSearchCardListener);
    }

    private OnSearchCardListener mRfCardSearchCardListener = new OnSearchCardListener() {
        @Override
        public void onCardInfo(CardInfoEntity cardInfoEntity) {
            mProgressDialog.dismiss();
            final byte rfCardType = cardInfoEntity.getRfCardType();
            readRfCard(rfCardType);
//            byte[] uid = cardInfoEntity.getRFuid();
//            final String uidString = StringUtils.convertBytesToHex(uid);
//            Log.d(TAG, "UID = " + uidString);
//            CardFragment.this.getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    DialogUtils.show(getActivity(), "Read contactless card result",
//                            "Card type: " + rfCardTypeToString(rfCardType) + "\n" +
//                                    "Result: UID = " + uidString);
//                }
//            });
        }

        @Override
        public void onError(int i) {
            mProgressDialog.dismiss();
            showReadRfCardErrorDialog(i);
        }

        @Override
        public void onNoCard(CardReaderTypeEnum cardReaderTypeEnum, boolean b) {

        }
    };

    public static final byte[] APDU_SEND_RF = {0x00, (byte) 0xA4, 0x04, 0x00, 0x0E, 0x32, 0x50, 0x41, 0x59, 0x2E, 0x53, 0x59, 0x53, 0x2E, 0x44, 0x44, 0x46, 0x30, 0x31, 0x00};
    private void readRfCard(final byte rfCardType) {
        RfCard rfCard = mCardReadManager.getRFCard();
        int result = rfCard.rfReset();
        if(result == SdkResult.SDK_OK) {
            byte[] apduSend = APDU_SEND_RF;
            int[] recvLen = new int[1];
            byte[] recvData = new byte[300];
            result = rfCard.rfExchangeAPDU(apduSend, recvData, recvLen);
            if(result == SdkResult.SDK_OK) {
                final String apduRecv = StringUtils.convertBytesToHex(recvData).substring(0, recvLen[0] * 2);
                CardFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.show(getActivity(), "Read contactless card result",
                                "Card type: " + rfCardTypeToString(rfCardType) + "\n" +
                                "Result: " + apduRecv);
                    }
                });
            } else {
                showReadRfCardErrorDialog(result);
            }
        } else {
            showReadRfCardErrorDialog(result);
        }
        rfCard.rfCardPowerDown();
    }

    private void showReadRfCardErrorDialog(final int errorCode) {
        CardFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtils.show(getActivity(), "Read contactless card failed", "Error code = " + errorCode);
            }
        });
    }

    //FELICA
    private void searchFelicaCard() {
        showSearchCardDialog(R.string.waiting, R.string.msg_felica_card);
        mCardReadManager.cancelSearchCard();
        mCardReadManager.searchCard(CardReaderTypeEnum.RF_CARD, READ_TIMEOUT, SdkData.RF_TYPE_FELICA, mFelicaCardSearchCardListener);
    }

    private OnSearchCardListener mFelicaCardSearchCardListener = new OnSearchCardListener() {
        @Override
        public void onCardInfo(CardInfoEntity cardInfoEntity) {
            mProgressDialog.dismiss();
            final byte rfCardType = cardInfoEntity.getRfCardType();
            readFelicaCard(rfCardType);
        }

        @Override
        public void onError(int i) {
            mProgressDialog.dismiss();
            showReadFelicaCardErrorDialog(i);
        }

        @Override
        public void onNoCard(CardReaderTypeEnum cardReaderTypeEnum, boolean b) {

        }
    };

    public static final byte[] APDU_SEND_FELICA = {0x10, 0x06, 0x01, 0x2E, 0x45, 0x76, (byte) 0xBA, (byte) 0xC5, 0x45, 0x2B, 0x01, 0x09, 0x00, 0x01, (byte) 0x80, 0x00};
    private void readFelicaCard(final byte rfCardType) {
        RfCard rfCard = mCardReadManager.getRFCard();
        int result = rfCard.rfReset();
        if(result == SdkResult.SDK_OK) {
            byte[] apduSend = APDU_SEND_FELICA;
            int[] recvLen = new int[1];
            byte[] recvData = new byte[300];
            result = rfCard.rfExchangeAPDU(apduSend, recvData, recvLen);
            if(result == SdkResult.SDK_OK) {
                final String apduRecv = StringUtils.convertBytesToHex(recvData).substring(0, recvLen[0] * 2);
                CardFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.show(getActivity(), "Read felica card result",
                                "Card type: " + rfCardTypeToString(rfCardType) + "\n" +
                                        "Result: " + apduRecv);
                    }
                });
            } else {
                showReadFelicaCardErrorDialog(result);
            }
        } else {
            showReadFelicaCardErrorDialog(result);
        }
        rfCard.rfCardPowerDown();
    }

    private void showReadFelicaCardErrorDialog(final int errorCode) {
        CardFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtils.show(getActivity(), "Read felica card failed", "Error code = " + errorCode);
            }
        });
    }

    //read M1
    private Dialog mReadM1Dialog;
    private View mReadM1DialogView;
    private String M1cardPassword = "FFFFFFFFFFFF";
    private byte M1cardKeyType = 0x00; // 0x00 typeA, 0x01 typeB
    private void showReadM1Dialog(Context context) {
        if(mReadM1DialogView == null) {
            mReadM1DialogView = getActivity().getLayoutInflater().inflate(
                    R.layout.read_m1_dialog, null);
        }
        if(mReadM1Dialog == null) {
            mReadM1Dialog = buildReadM1Dialog(context);
        }
        mReadM1Dialog.show();
    }

    private Dialog buildReadM1Dialog(Context context) {
        Dialog dialog;
        final EditText password = mReadM1DialogView.findViewById(R.id.password);
        final EditText key_type = mReadM1DialogView.findViewById(R.id.key_type);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(mReadM1DialogView)
                .setTitle(R.string.pref_read_M1_card)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        M1cardPassword = password.getText().toString().trim();
                        String keyType = key_type.getText().toString().trim();
                        M1cardKeyType = StringUtils.convertHexToBytes(keyType)[0];
                        showSearchCardDialog(R.string.pref_read_M1_card, R.string.msg_read_m1_card);
                        mCardReadManager.cancelSearchCard();
                        mCardReadManager.searchCard(CardReaderTypeEnum.RF_CARD,READ_TIMEOUT,SdkData.RF_TYPE_A,mReadM1cardSearchCardListener);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialog = dialogBuilder.create();
        return dialog;
    }

    private OnSearchCardListener mReadM1cardSearchCardListener = new OnSearchCardListener() {
        @Override
        public void onCardInfo(CardInfoEntity cardInfoEntity) {
            mProgressDialog.dismiss();
            readM1Card();
        }

        @Override
        public void onError(int i) {
            mProgressDialog.dismiss();
            showReadM1CardErrorDialog(i);
        }

        @Override
        public void onNoCard(CardReaderTypeEnum cardReaderTypeEnum, boolean b) {

        }
    };

    private void readM1Card() {
        StringBuilder m1_message = new StringBuilder();
        byte[] key = StringUtils.convertHexToBytes(M1cardPassword);
        RfCard rfCard = mCardReadManager.getRFCard();
        //One M1 card has a storage capacity of 1KB (16 sectors * 4 blocks * 16 bytes = 1024 bytes = 1KB)
        //now, we read the information of the 11th sectors.You can try to read the other sectors
        int sector = 10;
        int result = rfCard.m1VerifyKey((byte) (4 * sector), M1cardKeyType, key);
        if(result == SdkResult.SDK_OK) {
            for (int i = 0; i < 4; i++) {
                byte[] out = new byte[16];
                result = rfCard.m1ReadBlock((byte) (4 * sector + i), out);
                if (result == SdkResult.SDK_OK) {
                    m1_message.append("\nBlock").append(i).append(":")
                            .append(StringUtils.convertBytesToHex(out));
                } else {
                    break;
                }
            }
            if(result == SdkResult.SDK_OK) {
                final String resultString = m1_message.toString();
                CardFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.show(getActivity(), "Read M1 card result",
                                "Result : " + resultString);
                    }
                });
            } else {
                showReadM1CardErrorDialog(result);
            }
        } else {
            showReadM1CardErrorDialog(result);
        }
        rfCard.rfCardPowerDown();
    }

    private void showReadM1CardErrorDialog(final int errorCode) {
        CardFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtils.show(getActivity(), "Read M1 card failed", "Error code = " + errorCode);
            }
        });
    }

    //write M1
    private Dialog mWriteM1Dialog;
    private View mWriteM1DialogView;
    private String mWriteM1cardPassword = "FFFFFFFFFFFF";
    private byte mWriteM1cardKeyType = 0x00; // 0x00 typeA, 0x01 typeB
    private String mWriteM1cardContent = "";
    private void showWriteM1Dialog(Context context) {
        if(mWriteM1DialogView == null) {
            mWriteM1DialogView = getActivity().getLayoutInflater().inflate(
                    R.layout.write_m1_dialog, null);
        }
        if(mWriteM1Dialog == null) {
            mWriteM1Dialog = buildWriteM1Dialog(context);
        }
        mWriteM1Dialog.show();
    }

    private Dialog buildWriteM1Dialog(Context context) {
        Dialog dialog;
        final EditText password = mWriteM1DialogView.findViewById(R.id.password);
        final EditText key_type = mWriteM1DialogView.findViewById(R.id.key_type);
        final EditText contentEditText = mWriteM1DialogView.findViewById(R.id.write_context);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(mWriteM1DialogView)
                .setTitle(R.string.pref_write_M1_card)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mWriteM1cardPassword = password.getText().toString().trim();
                        String keyType = key_type.getText().toString().trim();
                        mWriteM1cardKeyType = StringUtils.convertHexToBytes(keyType)[0];
                        mWriteM1cardContent = contentEditText.getText().toString().trim();
                        showSearchCardDialog(R.string.pref_write_M1_card, R.string.msg_read_m1_card);
                        mCardReadManager.cancelSearchCard();
                        mCardReadManager.searchCard(CardReaderTypeEnum.RF_CARD,READ_TIMEOUT,SdkData.RF_TYPE_A, mWriteM1cardSearchCardListener);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialog = dialogBuilder.create();
        return dialog;
    }

    private OnSearchCardListener mWriteM1cardSearchCardListener = new OnSearchCardListener() {

        @Override
        public void onCardInfo(CardInfoEntity cardInfoEntity) {
            mProgressDialog.dismiss();
            writeM1();
        }

        @Override
        public void onError(int i) {
            showWriteM1CardErrorDialog(i);
        }

        @Override
        public void onNoCard(CardReaderTypeEnum cardReaderTypeEnum, boolean b) {

        }
    };

    private void writeM1() {
        byte[] key = StringUtils.convertHexToBytes(mWriteM1cardPassword);
        RfCard rfCard = mCardReadManager.getRFCard();
        int sector = 10; //11th sector
        int block = 1; //2nd block
        int result = rfCard.m1VerifyKey((byte) (4 * sector), mWriteM1cardKeyType, key);
        if (result == SdkResult.SDK_OK) {
            //write to 11th sector 1th block
            //Please note: The length of the input string must be even.
            byte[] input = StringUtils.convertHexToBytes(mWriteM1cardContent);
            input = fillWholeBlock(input);
            Log.d(TAG, "input array = " + Arrays.toString(input));
            result = rfCard.m1WirteBlock((byte) (4 * sector + block), input);
            if (result == SdkResult.SDK_OK) {
                CardFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.show(getActivity(), "Write M1 card success. Please use read M1 function to check");
                    }
                });
            } else {
                showWriteM1CardErrorDialog(result);
            }
        } else {
            showWriteM1CardErrorDialog(result);
        }
        rfCard.rfCardPowerDown();
    }

    private byte[] fillWholeBlock(byte[] source) {
        if(source == null || source.length == 0) {
            return new byte[16];
        }
        byte result[] = new byte[16];
        int length = source.length;
        for(int i = 0; i < length; i ++) {
            result[i] = source[i];
        }
        return result;
    }

    private void showWriteM1CardErrorDialog(final int errorCode) {
        CardFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtils.show(getActivity(), "Write M1 card failed", "Error code = " + errorCode);
            }
        });
    }

    //read mifare plus
    private Dialog mReadMifarePlusDialog;
    private View mReadMifarePlusDialogView;
    private String mMifarePlusCardPassword = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
    private byte[] mMifarePlusCardAddress = {0x40, 0x00};
    private void showReadMifarePlusDialog(Context context) {
        if(mReadMifarePlusDialogView == null) {
            mReadMifarePlusDialogView = getActivity().getLayoutInflater().inflate(
                    R.layout.read_mifare_plus_dialog, null);
        }
        if(mReadMifarePlusDialog == null) {
            mReadMifarePlusDialog = buildReadMifarePlusDialog(context);
        }
        mReadMifarePlusDialog.show();
    }

    private Dialog buildReadMifarePlusDialog(Context context) {
        Dialog dialog;
        final EditText password = mReadMifarePlusDialogView.findViewById(R.id.password);
        final EditText keyAddressEditText = mReadMifarePlusDialogView.findViewById(R.id.key_address);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(mReadMifarePlusDialogView)
                .setTitle(R.string.pref_read_Mifare_Plus_card_key)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMifarePlusCardPassword = password.getText().toString().trim();
                        String keyAddress = keyAddressEditText.getText().toString().trim();
                        mMifarePlusCardAddress = StringUtils.convertHexToBytes(keyAddress);
                        showSearchCardDialog(R.string.pref_read_Mifare_Plus_card_key, R.string.msg_read_mifare_plus_card);
                        mCardReadManager.cancelSearchCard();
                        mCardReadManager.searchCard(CardReaderTypeEnum.RF_CARD,READ_TIMEOUT,SdkData.RF_TYPE_A,mReadMifarePlusCardSearchCardListener);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialog = dialogBuilder.create();
        return dialog;
    }

    private OnSearchCardListener mReadMifarePlusCardSearchCardListener = new OnSearchCardListener() {
        @Override
        public void onCardInfo(CardInfoEntity cardInfoEntity) {
            mProgressDialog.dismiss();
            readMifarePlusCard();
        }

        @Override
        public void onError(int i) {
            mProgressDialog.dismiss();
            showReadMifarePlusCardErrorDialog(i);
        }

        @Override
        public void onNoCard(CardReaderTypeEnum cardReaderTypeEnum, boolean b) {

        }
    };

    private void readMifarePlusCard() {
        RfCard rfCard = mCardReadManager.getRFCard();
        StringBuilder mifarePlusResultMessage = new StringBuilder();
        byte[] key = StringUtils.convertHexToBytes(mMifarePlusCardPassword);
        int result = rfCard.mFPlusFirstAuthen(mMifarePlusCardAddress, key);
        if (result == SdkResult.SDK_OK) {
            mifarePlusResultMessage.append("Read sector 0:");
            byte[] outdata = new byte[64];
            result = rfCard.mFPlusL3Read(StringUtils.convertHexToBytes("0000"), (byte) 4, outdata);
            if(result == SdkResult.SDK_OK) {
                mifarePlusResultMessage.append(StringUtils.convertBytesToHex(outdata));
                final String resultString = mifarePlusResultMessage.toString();
                CardFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.show(getActivity(), "Read Mifara plus card result",
                                "Result : " + resultString);
                    }
                });
            } else {
                showReadMifarePlusCardErrorDialog(result);
            }
        } else {
            showReadMifarePlusCardErrorDialog(result);
        }
        rfCard.rfCardPowerDown();
    }

    private void showReadMifarePlusCardErrorDialog(final int errorCode) {
        CardFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DialogUtils.show(getActivity(), "Read Mifare plus card failed", "Error code = " + errorCode);
            }
        });
    }

    // common method
    private void showSearchCardDialog(@StringRes int title, @StringRes int msg) {
        mProgressDialog = (ProgressDialog) DialogUtils.showProgress(getActivity(), getString(title), getString(msg), new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mCardReadManager.cancelSearchCard();
            }
        });
    }

    private static String cardInfoToString(CardInfoEntity cardInfoEntity) {
        if (cardInfoEntity == null)
            return null;
        StringBuilder sb = new StringBuilder();
        sb.append("Resultcode:\t" + cardInfoEntity.getResultcode() + "\n")
                .append(cardInfoEntity.getCardExistslot() == null ? "" : "Card type:\t" + cardInfoEntity.getCardExistslot().name() + "\n")
                .append(cardInfoEntity.getCardNo() == null ? "" : "Card no:\t" + cardInfoEntity.getCardNo() + "\n")
                .append(cardInfoEntity.getRfCardType() == 0 ? "" : "Rf card type:\t" + cardInfoEntity.getRfCardType() + "\n")
                .append(cardInfoEntity.getRFuid() == null ? "" : "RFUid:\t" + new String(cardInfoEntity.getRFuid()) + "\n")
                .append(cardInfoEntity.getAtr() == null ? "" : "Atr:\t" + cardInfoEntity.getAtr() + "\n")
                .append(cardInfoEntity.getTk1() == null ? "" : "Track1:\t" + cardInfoEntity.getTk1() + "\n")
                .append(cardInfoEntity.getTk2() == null ? "" : "Track2:\t" + cardInfoEntity.getTk2() + "\n")
                .append(cardInfoEntity.getTk3() == null ? "" : "Track3:\t" + cardInfoEntity.getTk3() + "\n")
                .append(cardInfoEntity.getExpiredDate() == null ? "" : "expiredDate:\t" + cardInfoEntity.getExpiredDate() + "\n")
                .append(cardInfoEntity.getServiceCode() == null ? "" : "serviceCode:\t" + cardInfoEntity.getServiceCode());
        return sb.toString();
    }

    private String rfCardTypeToString(byte rfCardType) {
        String type = "";
        switch (rfCardType) {
            case SdkData.RF_TYPE_A:
                type = "RF_TYPE_A";
                break;
            case SdkData.RF_TYPE_B:
                type = "RF_TYPE_B";
                break;
            case SdkData.RF_TYPE_MEMORY_A:
                type = "RF_TYPE_MEMORY_A";
                break;
            case SdkData.RF_TYPE_FELICA:
                type = "RF_TYPE_FELICA";
                break;
            case SdkData.RF_TYPE_MEMORY_B:
                type = "RF_TYPE_MEMORY_B";
                break;
        }
        return type;
    }

    private void readThailandCard() {
        byte[] CID = new byte[13];
        byte[] nameSex = new byte[209];
        byte[] address = new byte[100];
        byte[] issueExpire = new byte[18];
        int ret = mCardReadManager.getThailandIDInfo((byte)0, CID, nameSex, address, issueExpire);
        if (ret == SdkResult.SDK_OK) {
            final String CIDString = StringUtils.convertBytesToHex(CID);
            final String nameSexString = StringUtils.convertBytesToHex(nameSex);
            final String addressString = StringUtils.convertBytesToHex(address);
            final String issueExpireString = StringUtils.convertBytesToHex(issueExpire);
            final String message = "CID: " + CIDString +  "\n" +
                    "NameSex: " + nameSexString + "\n" +
                    "Address: " + addressString + "\n" +
                    "IssueExpire" + issueExpireString;
            DialogUtils.show(this.getActivity(), "Read result", message);
        } else {
            DialogUtils.show(this.getActivity(), "Read result", "Read Failed!");
        }
    }
}
