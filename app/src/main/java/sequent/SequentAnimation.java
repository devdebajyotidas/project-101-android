package sequent;


import com.example.newproject.R;

/**
 * Created by y_fujikawa on 2017/05/26.
 */

public enum SequentAnimation {
    BOUNCE_IN(R.anim.bounce_in),
    FADE_IN(R.anim.fade_in),
    FADE_IN_DOWN(R.anim.fade_in_down),
    FADE_IN_UP(R.anim.fade_in_up),
    FADE_IN_LEFT(R.anim.fade_in_left),
    FADE_IN_RIGHT(R.anim.fade_in_right),
    ROTATE_IN(R.anim.rotate_in),;

    private int animId;

    SequentAnimation(int animId) {
        this.animId = animId;
    }

    public int getAnimId() {
        return this.animId;
    }
}
