# A smart car controller

## App Introduction 

This app is an Android program for smart car control. It can send control commands to the smart car and receive information fed back by the car. So if you want use the App, you should write the relevant control program of the smart car first! :smile:This App mainly communicates with the smart car through the Bluetooth Serial Port. You just need to complete the Bluetooth pairing to get started


![YTAPP](https://user-images.githubusercontent.com/70866844/181872238-2ee0801e-fcf4-4acc-b834-da1de2523f00.png)

## How to use it

If you want to use it yourself, it is strongly recommended to modify the source code. The actual application scenario of this App is the control of the smart stroller. Therefore, the App is limited by the following limitations in the process of use.

- Display paired devices, only bluetooth devices starting with ```YT``` can be displayed. Because we don't want other unrelated devices to interfere with the user's choices.
- The About page is an introduction to the author's development team ([HUST-FOCUS](https://github.com/HUST-FOCUS)). you can replace it. This page uses open source libraries: [https://github.com/wcoder/AndroidAboutPage](https://github.com/wcoder/AndroidAboutPage).
- The software has three control modes: **Automatic Tracking**, **Remote Control Mode**, **One-key Recall**. 
- The rocker can  only send 7 commands: forward 1, forward 2, forward 3, backward, turn left, turn right, stop.

## To be continued ~~~🎦 😃 
