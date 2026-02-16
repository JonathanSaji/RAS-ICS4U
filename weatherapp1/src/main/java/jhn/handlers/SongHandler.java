package jhn.handlers;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SongHandler {

    Clip audioClip;

    public SongHandler(String audioFilePath) {
        try {
            File audioFile = new File(audioFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void play() {
        if (audioClip != null) {
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            audioClip.start();
        }
    }

    public void stop() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
        }
    }

    public static void main(String[] args) {
        // Example usage
        SongHandler songHandler = new SongHandler("weatherapp1/src/main/java/jhn/resources/KCDII.wav");
        songHandler.play();
        try {
            Thread.sleep(100000); // Play for 10 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        songHandler.stop();
    }
}
