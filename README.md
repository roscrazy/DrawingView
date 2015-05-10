SignView
========

An Android custom view which let user signing or drawing and it's will be saved to an output stream.
The special thing is you can draw a smooth line and the stroke size will change gradually depend on your speed.
There are two values you should care about: 

  - sign:filterWeight="0.2"
  - sign:strokeSize="@dimen/signWidth" 

1. filterWeight : How the stroke size will be changed when user change his finger speed(this value effect on the lowpass filter to mitigate velocity aberrations). You can set this value from 0 -> 1, and figure out which value suit for your app.
2. strokeSize : The biggest stroke size when user drawing.


And one more thing : To draw smooth line, I use the floating point number so, it's kind of difficult to optimise the view(ex: using bitwise operators...) 
  
  
Example :

![alt tag](http://s28.postimg.org/z94yvlyx9/1415638175277.png)

![alt tag](http://s8.postimg.org/gng33qtx1/device_2014_11_11_001550.png)
