Pitch detection
===============

The main purpose of this github repository is to show how you can detect a pitch of the sound.
I will try to describe all the steps to achieve this in a simple manner.

How to use
----------

```java
// Create audio input object to capture the sound.
// For desktop/laptop computers you can use MicrophoneAudioInput class.
AudioInputInterface audioInput = MicrophoneAudioInput.getAudioInput();

// Create frequency detector object.
FundamentalFrequencyDetector frequencyDetector = new FFTFundamentalFrequencyDetector(audioInput, 4096);

// Create tuner object.
GuitarTuner tuner = new DefaultGuitarTuner(frequencyDetector);

// Finally start the tuner and get results, then stop the tuner.
tuner.start();
TuneInfo info = tuner.tune();
System.out.println(info.note);
System.out.println(info.delta);
tuner.stop();
```


[![Build Status](https://travis-ci.org/undertext/pitch_detection.svg?branch=master)](https://travis-ci.org/undertext/pitch_detection)
[![codecov](https://codecov.io/gh/undertext/pitch_detection/branch/master/graph/badge.svg)](https://codecov.io/gh/undertext/pitch_detection)