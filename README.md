# zcsdemo
Z100 Fast-running POS with 80mm Built-in Thermal Printer and 2D Bar Code Scanner
This document is applicable to the Android platform SDK of Smart POS (Z90, Z91, Z92, Z100), MPOS (Z70), all-in-one card reader module (Z45) and other products.
This document uses Android Studio as the development environment by default. If you use other development platforms to develop, please refer to the environment of the corresponding platform. 
This document is for reference only. 

1. Import jar package Copy the ‘SmartPos_xxx.jar’ file to the ‘app\libs’ directory. After the copy is complete, click the jar package, right-click—>add as library. 
If you need to use the function of printing QR code, you also need to import the jar package of ‘zxing’, that is, copy the ‘core-3.2.1.jar’ file to the ‘app\libs’ directory. After the copy is complete, click the jar package and right-click—>add as library. 
2. Import so library Copy the ‘armeabi-v7a’ and ‘arm64-v8a’ directories to the ‘src/main/jniLibs’ directory. The so library includes ‘libSmartPosJni.so’ and ‘libEmvCoreJni.so’. 
Among them, ‘libSmartPosJni.so’ is the basic so, and ‘libEmvCoreJni.so’ is the so related to the EMV function. If the EMV function is not required, ‘libEmvCoreJni.so’ can not be added.
