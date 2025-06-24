import java.util.Scanner;
import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class PlaySound {
private static Clip gameClip;

    public static void playGameStartSound() {
        try {
        File soundFile = new File("C:\\Users\\ASUS\\Documents\\FP_Dasprog\\sounds\\Opening.wav");

        gameClip  = AudioSystem.getClip();
        gameClip.open(AudioSystem.getAudioInputStream(soundFile));
        gameClip.start();
        gameClip.loop(Clip.LOOP_CONTINUOUSLY);
        

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
        playSingleSound("C:\\Users\\ASUS\\Documents\\FP_Dasprog\\sounds\\Win.wav");
    }

    public static void playLoseSound() {
        playSingleSound("C:\\Users\\ASUS\\Documents\\FP_Dasprog\\sounds\\Lose.wav");
    }

    public static void playDrawSound() {
        try {
        File soundFile = new File("C:\\Users\\ASUS\\Documents\\FP_Dasprog\\sounds\\Draw.wav");

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
