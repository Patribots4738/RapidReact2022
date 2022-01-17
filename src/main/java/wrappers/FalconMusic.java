package wrappers;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.music.Orchestra;

public class FalconMusic {

    ArrayList<TalonFX> instruments;
    Orchestra orchestra;

    public FalconMusic(Falcon[] motors) {

        instruments = new ArrayList<>();

        for (int i = 0; i < motors.length; i++) {

            instruments.add(motors[i].getTalonFX());

        }

        orchestra = new Orchestra(instruments);

    }

    /**
     * place song in deploy folder
     * @param song is .chrp file, make a .chrp from a .MIDI inside of phoenix tuner, son should be in the format of "song.chrp"
     */
    public void loadSong(String song) {

        orchestra.loadMusic(song);

    }
    
    public void play() {

        try {

            wait(200);

        } catch (Exception e) {


        }
        
        orchestra.play();

    }

    public void pause() {

        orchestra.pause();

    }

    public void stop() {

        orchestra.stop();

    }

    public boolean isPlaying() {

        return orchestra.isPlaying();

    }

    public void addFalcon(Falcon falcon) {

        orchestra.addInstrument(falcon.getTalonFX());

    }

    public void addFalcons(Falcon[] falcons) {

        for (Falcon falcon : falcons) {

            orchestra.addInstrument(falcon.getTalonFX());

        }

    }

}
