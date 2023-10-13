package com.zcs.zcssdkdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.support.v4.print.PrintHelper;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.Printer;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.print.PrnStrFormat;
import com.zcs.sdk.print.PrnTextFont;
import com.zcs.sdk.print.PrnTextStyle;
import com.zcs.zcssdkdemo.utils.BitmapUtils;
import com.zcs.zcssdkdemo.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class PrintFragment extends PreferenceFragment {

    private static final String KEY_COMMON_PRINT_CATEGORY = "common_print_paper_key";
    private static final String KEY_PRINT_TEXT = "print_text_key";
    private static final String KEY_PRINT_QRCODE = "print_qrcode_key";
    private static final String KEY_PRINT_BARCODE = "print_barcode_key";
    private static final String KEY_PRINT_BITMAP = "print_bitmap_key";
    private static final String KEY_CUT_PAPER = "cut_paper_key";

    private static final String KEY_PRINT_LABEL = "print_lable_key";

    private static final String KEY_PRINT_WITH_SERVICE = "print_with_android_service_key";
    private static final String KEY_CONNECT_BLUETOOTH = "connect_bluetooth";
    private static final String KEY_PRINT_TEXT_WITH_BLUETOOTH = "print_text_with_bluetooth_key";
    private static final String KEY_PRINT_QRCODE_WITH_BLUETOOTH = "print_qrcode_with_bluetooth_key";
    private static final String KEY_PRINT_BARCODE_WITH_BLUETOOTH = "print_barcode_with_bluetooth_key";
    private static final String KEY_PRINT_BITMAP_WITH_BLUETOOTH = "print_bitmap_with_bluetooth_key";
    private static final String TAG = "PrintFragment";

    private DriverManager mDriverManager;
    private Printer mPrinter;

    private boolean isSupportCutter = false;

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_print);
        mContext = getActivity();
        mDriverManager = DriverManager.getInstance();
        mPrinter = mDriverManager.getPrinter();
        isSupportCutter = mPrinter.isSuppoerCutter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findPreference(KEY_PRINT_TEXT).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                printText();
                return true;
            }
        });

        findPreference(KEY_PRINT_QRCODE).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                printQrcode("www.google.com");
                return true;
            }
        });

        findPreference(KEY_PRINT_BARCODE).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                printBarCode128("6922711079066");
                return true;
            }
        });

        findPreference(KEY_PRINT_BITMAP).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                InputStream inputStream = null;
                try {
                    inputStream = getActivity().getAssets().open("print_demo.bmp");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(inputStream == null) {
                    return true;
                }
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                printBitmap(bitmap);
                return true;
            }
        });

        findPreference(KEY_PRINT_LABEL).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                InputStream inputStream = null;
                try {
                    inputStream = getActivity().getAssets().open("label_photo_demo.bmp");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(inputStream == null) {
                    return true;
                }
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                printLabel(bitmap);
                return true;
            }
        });

        findPreference(KEY_PRINT_WITH_SERVICE).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                InputStream inputStream = null;
                try {
                    inputStream = getActivity().getAssets().open("label_photo_demo.bmp");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(inputStream == null) {
                    return true;
                }
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                printWithService(bitmap);
                return true;
            }
        });

        findPreference(KEY_CONNECT_BLUETOOTH).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                scanAndConnectBluetooth();
                return true;
            }
        });

        findPreference(KEY_PRINT_TEXT_WITH_BLUETOOTH).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                printTextWithBluetooth("Print text with bluetooth printer\n");
                return true;
            }
        });

        findPreference(KEY_PRINT_QRCODE_WITH_BLUETOOTH).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                printQRCodeWithBluetooth("www.google.com");
                return true;
            }
        });

        findPreference(KEY_PRINT_BARCODE_WITH_BLUETOOTH).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                printBarCodeWithBluetooth("6922711079066", CODE128, 48);
                return true;
            }
        });

        findPreference(KEY_PRINT_BITMAP_WITH_BLUETOOTH).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                InputStream inputStream = null;
                try {
                    inputStream = getActivity().getAssets().open("label_photo_demo.bmp");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(inputStream == null) {
                    return true;
                }
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                printBitmapWithBluetooth(bitmap);
                return true;
            }
        });

        Preference cutPreference = findPreference(KEY_CUT_PAPER);
        cutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                cutPaper();
                return true;
            }
        });
        if(!isSupportCutter) {
            ((PreferenceGroup)findPreference(KEY_COMMON_PRINT_CATEGORY)).removePreference(cutPreference);
        }
    }
    
    private void printText() {
        int printStatus = mPrinter.getPrinterStatus();
        if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
            //out of paper
            Toast.makeText(getActivity(), "Out of paper", Toast.LENGTH_SHORT).show();
        } else {
            PrnStrFormat format = new PrnStrFormat();
            format.setTextSize(30);
            format.setAli(Layout.Alignment.ALIGN_CENTER);
            format.setStyle(PrnTextStyle.BOLD);
            //format.setFont(PrnTextFont.CUSTOM);
            //format.setPath(Environment.getExternalStorageDirectory() + "/fonts/simsun.ttf");
            format.setFont(PrnTextFont.SANS_SERIF);
            mPrinter.setPrintAppendString("POS SALES SLIP", format);
            format.setTextSize(25);
            format.setStyle(PrnTextStyle.NORMAL);
            format.setAli(Layout.Alignment.ALIGN_NORMAL);
            mPrinter.setPrintAppendString(" ", format);
            mPrinter.setPrintAppendString("MERCHANGT NAME:" + " Test ", format);
            mPrinter.setPrintAppendString("MERCHANT NO:" + " 123456789012345 ", format);
            mPrinter.setPrintAppendString("TERMINAL NAME:" + " 12345678 ", format);
            mPrinter.setPrintAppendString("OPERATOR NO:" + " 01 ", format);
            mPrinter.setPrintAppendString("CARD NO: ", format);
            format.setAli(Layout.Alignment.ALIGN_CENTER);
            format.setTextSize(30);
            format.setStyle(PrnTextStyle.BOLD);
            mPrinter.setPrintAppendString("6214 44** **** **** 7816", format);
            format.setAli(Layout.Alignment.ALIGN_NORMAL);
            format.setStyle(PrnTextStyle.NORMAL);
            format.setTextSize(25);
            mPrinter.setPrintAppendString(" -----------------------------", format);
            mPrinter.setPrintAppendString(" ", format);
            mPrinter.setPrintAppendString(" ", format);
            mPrinter.setPrintAppendString(" ", format);
            mPrinter.setPrintAppendString(" ", format);
            printStatus = mPrinter.setPrintStart();
        }
    }
    
    private void printQrcode(String qrString) {
        int printStatus = mPrinter.getPrinterStatus();
        if (printStatus != SdkResult.SDK_PRN_STATUS_PAPEROUT) {
            mPrinter.setPrintAppendQRCode(qrString, 200, 200, Layout.Alignment.ALIGN_CENTER);
            printStatus = mPrinter.setPrintStart();
        }
    }
    
    private void printBarCode128(String barcodeString) {
        int printStatus = mPrinter.getPrinterStatus();
        if (printStatus != SdkResult.SDK_PRN_STATUS_PAPEROUT) {
            mPrinter.setPrintAppendBarCode(getActivity(), barcodeString, 360, 100, true, Layout.Alignment.ALIGN_CENTER, BarcodeFormat.CODE_128);
            printStatus = mPrinter.setPrintStart();
        }
    }

    private void printBitmap(Bitmap bitmap) {
        int printStatus = mPrinter.getPrinterStatus();
        if (printStatus != SdkResult.SDK_PRN_STATUS_PAPEROUT) {
            mPrinter.setPrintAppendBitmap(bitmap, Layout.Alignment.ALIGN_CENTER);
            printStatus = mPrinter.setPrintStart();
        }
    }

    private void cutPaper() {
        int printStatus = mPrinter.getPrinterStatus();
        if(printStatus == SdkResult.SDK_OK) {
            mPrinter.openPrnCutter((byte) 1);
        }
    }

    private void printLabel(Bitmap bitmap) {
        int printStatus = mPrinter.getPrinterStatus();
        if (printStatus != SdkResult.SDK_PRN_STATUS_PAPEROUT) {
            mPrinter.printLabel(bitmap);
        }
    }

    private void printWithService(Bitmap bitmap) {
        PrintHelper printHelper = new PrintHelper(getActivity());
        printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        printHelper.printBitmap("TestDemo", bitmap);
    }

    //below for BT print
    BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BLUETOOTH = 10086;
    private static final String BLUETOOTH_ADDRESS = "66:11:22:33:44:55";
    private boolean isScanning = false;
    private boolean isConnecting = false;
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mBluetoothSocket;
    private OutputStream mOutputStream;
    private void scanAndConnectBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            Log.e(TAG, "Device not support bluetooth");
            return;
        }
        //open bluetooth
        if(!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
        } else {
            startScanBluetooth();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if(mBluetoothAdapter.isEnabled()) {
                startScanBluetooth();
            } else {
                Log.e(TAG, "Bluetooth not open");
            }
        }
    }

    private void startScanBluetooth() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        Log.d(TAG, "pairedDevices size = " + pairedDevices.size());
        BluetoothDevice pairedDevice = null;
        if (pairedDevices.size() > 0) {
            Log.d(TAG, "pairedDevices");
            for (BluetoothDevice device : pairedDevices) {
                if(isZcsBluetoothPrinter(device)) {
                    pairedDevice = device;
                    break;
                }
            }
        }

        if(pairedDevice != null) {
            connectDevice(pairedDevice);
            return;
        }

        isScanning = true;
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mBluetoothFoundReceiver, filter);
        mBluetoothAdapter.startDiscovery();
    }

    private BroadcastReceiver mBluetoothFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "address = " + device.getAddress());
                if(isZcsBluetoothPrinter(device)) {
                    connectDevice(device);
                }
            }
        }
    };

    private void connectDevice(BluetoothDevice device) {
        Log.d(TAG, "isScanning = " + isScanning);
        if(isScanning) {
            mBluetoothAdapter.cancelDiscovery();
            getActivity().unregisterReceiver(mBluetoothFoundReceiver);
            isScanning = false;
        }
        Log.d(TAG, "connectDevice");
        if(isConnecting) {
            Log.d(TAG, "is scanning, wait.");
            return;
        } else {
            connectDeviceThread(device);
        }
    }

    private void connectDeviceThread(final BluetoothDevice device) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isConnecting = true;
                try {
                    mBluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
                    mBluetoothSocket.connect();
                    mOutputStream = mBluetoothSocket.getOutputStream();
                    Log.d(TAG, "Connect success");
                    PrintFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "Connect success", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isConnecting = false;
                }
            }
        }).start();
    }

    private boolean isZcsBluetoothPrinter(BluetoothDevice device) {
        String address = device.getAddress();
        if(TextUtils.equals(address, BLUETOOTH_ADDRESS)) {
            return true;
        }
        return false;
    }

    /**
     * Write to the printer
     *
     * @param buffer The bytes to write
     */
    public synchronized void write(byte[] buffer) {
        if(mOutputStream == null) {
            Log.e(TAG, "Not connect.");
            return;
        }
        int len = buffer.length;
        int idx = 0, count = 0;
        while (idx < len) {
            if (len - idx > 1024 * 100)
                count = 1024 * 100;
            else
                count = len - idx;
            try {
                mOutputStream.write(buffer, idx, count);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            idx = idx + count;
        }
    }

    /**
     * clear buffer data and reset printer
     */
    public void reset() {
        // reset
        write(RESET);
        // set font
        write(DEFAULT_SIZE);
        // set lang
        write(LANG_DEFAULT);
        flush();
    }

    /**
     * Flush to the printer
     */
    public void flush() {
        try {
            mOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static byte[] RESET = { 0x1b, 0x40 };
    public final static byte[] DEFAULT_SIZE = { 0x1d, 0x21, 00 };
    public final static byte[] LANG_DEFAULT = { 0x1B, 0x74, (byte) 0x80 };

    private void printTextWithBluetooth(String text) {
        write(text.getBytes());
    }

    private void printBitmapWithBluetooth(Bitmap bitmap) {
        printImage(bitmap);
    }

    /**
     * Print bitmap
     *
     * @param bitmap
     */
    public void printBitmap(Bitmap bitmap, boolean colored) {
        printImage(bitmap, colored);
    }

    private void printImage(Bitmap bitmap) {
        printImage(bitmap, false);
    }
    private void printImage(Bitmap bitmap, boolean colored) {
        if (bitmap != null && bitmap.getWidth() > 384) {
            bitmap = BitmapUtils.scaleBitmap(bitmap, (int) (bitmap.getWidth() / 384f));
        }
        byte[] command = new byte[]{0x1d, 0x76, 0x30, 0x00};
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bw = (width - 1) / 8 + 1;

        byte[] rv = new byte[height * bw + 4];
        rv[0] = (byte) bw;//xL
        rv[1] = (byte) (bw >> 8);//xH
        rv[2] = (byte) height;
        rv[3] = (byte) (height >> 8);

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int color = pixels[width * i + j];
                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;
                byte gray = BitmapUtils.rgb2Gray(r, g, b, colored);
                rv[bw * i + j / 8 + 4] = (byte) (rv[bw * i + j / 8 + 4] | (gray << (7 - j % 8)));
            }
        }
        write(StringUtils.concat(command, rv));
    }

    public void printQRCodeWithBluetooth(String qrCodeData) {
        byte[] command = new byte[]{0x1D, 0x28, 0x6B, (byte) (qrCodeData.length() + 3), 0x00, 0x31, 0x50, 0x30};
        command = StringUtils.concat(command, qrCodeData.getBytes());
        byte[] printStartCommand = new byte[]{0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30};
        command = StringUtils.concat(command, printStartCommand);

        // Line feed
        byte[] lineFeedCommand = new byte[]{0x0A};
        command = StringUtils.concat(command, lineFeedCommand);
        write(command);
    }

    public final static byte EAN13 = 67;
    public final static byte CODE128 = 73;

    public void printBarCodeWithBluetooth(String barcode, int type, int height) {
        byte[] data = barcode.getBytes();
        // Set barcode height
        write(new byte[] { 0x1d, 0x68, (byte) height });
        byte[] bytes = new byte[4 + data.length];
        bytes[0] = 0x1d;
        bytes[1] = 0x6b;
        bytes[2] = (byte) type;
        bytes[3] = (byte) data.length;
        for (int i = 4; i < data.length + 4; i++) {
            bytes[i] = data[i - 4];
        }
        write(bytes);
    }

}
