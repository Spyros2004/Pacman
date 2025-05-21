import javax.sound.sampled.*;
import java.io.IOException;

public class SoundManager {

	private Clip startMusic;
	private Clip pelletSound;
	private Clip lifeLostSound;
	private Clip successSound;
	private Clip gameOverSound;

	/**
	 * Constructor that initializes and loads all sound clips.
	 */
	public SoundManager() {
		// Load all sounds when the game starts
		loadSounds();
	}

	/**
	 * Loads all sound clips from resource files. Catches and prints exceptions if
	 * loading fails.
	 */
	private void loadSounds() {
		try {
			startMusic = loadSound("/res/sounds/start_music.wav");
			pelletSound = loadSound("/res/sounds/pellet_sound.wav");
			lifeLostSound = loadSound("/res/sounds/life_lost.wav");
			successSound = loadSound("/res/sounds/success.wav");
			gameOverSound = loadSound("/res/sounds/game_over.wav");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads a single sound clip from the given file path.
	 *
	 * @param path The resource path to the sound file.
	 * @return The loaded Clip object.
	 * @throws UnsupportedAudioFileException If the audio format is unsupported.
	 * @throws IOException                   If there is an I/O error.
	 * @throws LineUnavailableException      If a line cannot be opened.
	 */
	private Clip loadSound(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(path));
		Clip clip = AudioSystem.getClip();
		clip.open(audioIn);
		return clip;
	}

	/**
	 * Plays the background start music if not already playing. Resets to the
	 * beginning before playing.
	 */
	public void playStartMusic() {
		if (startMusic != null && !startMusic.isRunning()) {
			startMusic.setFramePosition(0);
			startMusic.start();
		}
	}

	/**
	 * Plays the pellet collection sound effect. Resets to the beginning before
	 * playing.
	 */
	public void playPelletSound() {
		if (pelletSound != null && !pelletSound.isRunning()) {
			pelletSound.setFramePosition(0); // Reset clip to start
			pelletSound.start(); // Play sound
		}
	}

	/**
	 * Plays the sound effect for losing a life. Resets to the beginning before
	 * playing.
	 */
	public void playLifeLostSound() {
		if (lifeLostSound != null && !lifeLostSound.isRunning()) {
			lifeLostSound.setFramePosition(0);
			lifeLostSound.start();
		}
	}

	/**
	 * Plays the success sound effect. Resets to the beginning before playing.
	 */
	public void playSuccessSound() {
		if (successSound != null && !successSound.isRunning()) {
			successSound.setFramePosition(0);
			successSound.start();
		}
	}

	/**
	 * Plays the game over sound effect. Resets to the beginning before playing.
	 */
	public void playGameOverSound() {
		if (gameOverSound != null && !gameOverSound.isRunning()) {
			gameOverSound.setFramePosition(0);
			gameOverSound.start();
		}
	}
}
