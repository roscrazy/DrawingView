SignView
========

An Android custom view which let user sign or draw and it's will be saved as a PNG file.
The special thing is you can draw a smooth line and the stroke size will change gradually depend on your speed.
There are two values you should care about: 

  - sign:filterWeight="0.2"
  - sign:strokeSize="@dimen/signWidth" 

1. filterWeight : How the stroke size will be changed when user change his finger speed(this value effect on the lowpass filter to mitigate velocity aberrations). You can set this value from 0 -> 1, and figure out which value suit for your app.
2. strokeSize : The biggest stroke size when user drawing.

Note : The image file in the example will be saved on your "sdcard/SignView" folder
        And you need ActionBarSherlock to run the example :  https://github.com/JakeWharton/ActionBarSherlock

Example :

![alt tag](http://s28.postimg.org/z94yvlyx9/1415638175277.png)

![alt tag](http://s8.postimg.org/gng33qtx1/device_2014_11_11_001550.png)
