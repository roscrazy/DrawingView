SignView
========

An Android custom view which let user sign or draw and it's will be saved as a PNG file.
The special thing is you can draw a smooth line and the stroke size will change gradually depend on your speed.
There are two values you should care about: 

  sign:filterWeight="0.2"
  sign:strokeSize="@dimen/signWidth" 

1. filterWeight : How the stroke size will be changed when user change his finger speed(this value effect on the lowpass filter to mitigate velocity aberrations). You can set this value from 0 -> 1, and figure out which value is suite for your app.
2. strokeSize : The biggest stroke size when user drawing.
