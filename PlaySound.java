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

        File soundFile = new File("./asset/sound/Opening.wav");

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
        playSingleSound("./asset/sound/Win.wav");
    }

    public static void playDrawSound() {
       playSingleSound("./asset/sound/draw.wav");
    }   

    private static void playSingleSound(String path) {
        try {
            File soundFile = new File(path);
            gameClip = AudioSystem.getClip();
            gameClip.open(AudioSystem.getAudioInputStream(soundFile));
            gameClip.start();
        } catch (Exception e) {
            System.out.println("Gagal memutar sound efek.");
            e.printStackTrace();
        }
    }
}
