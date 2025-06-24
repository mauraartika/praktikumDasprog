import java.util.Scanner;
import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlaySound {
private static Clip gameClip;

    public static void playGameStartSound() {
        try {

            if (gameClip != null && gameClip.isRunning()) {
                return;
            }

<<<<<<< HEAD
        File soundFile = new File("Sounds\\Opening.wav");
=======
        File soundFile = new File("./asset/sound/Opening.wav");
>>>>>>> 05e85a16d9570cefc68adc6821bf4759294a6c17

        gameClip  = AudioSystem.getClip();
        gameClip.open(AudioSystem.getAudioInputStream(soundFile));
        gameClip.loop(Clip.LOOP_CONTINUOUSLY);
        gameClip.start();
        

        } catch (Exception e) {
            System.out.println("Gagal memutar suara.");
            e.printStackTrace();
        }   
    }

    public static void stopGameSound() {
        if (gameClip  != null && gameClip.isRunning()) {
            gameClip.stop();
            gameClip.close();
        }
    }

    public static void playWinSound() {
<<<<<<< HEAD
        playSingleSound("Sounds\\Win.wav");
=======
        playSingleSound("./asset/sound/Win.wav");
>>>>>>> 05e85a16d9570cefc68adc6821bf4759294a6c17
    }

    public static void playDrawSound() {
        try {
<<<<<<< HEAD
        File soundFile = new File("Sounds\\Draw.wav");
=======
        File soundFile = new File("./asset/sound/Draw.wav");
>>>>>>> 05e85a16d9570cefc68adc6821bf4759294a6c17

        gameClip = AudioSystem.getClip();
        gameClip.open(AudioSystem.getAudioInputStream(soundFile));
        
        //crop the sound at 2:06 - 2:09
        gameClip.setMicrosecondPosition(126_000_000);
        gameClip.start();

            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    gameClip.stop();
                    gameClip.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            System.out.println("Gagal memutar draw sound");
            e.printStackTrace();
        }
    }   

    private static void playSingleSound(String path) {
        try {
            File soundFile = new File(path);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(soundFile));
            clip.start();
        } catch (Exception e) {
            System.out.println("Gagal memutar sound efek.");
            e.printStackTrace();
        }
    }
}
