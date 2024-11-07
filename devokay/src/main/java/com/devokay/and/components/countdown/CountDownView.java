package com.devokay.and.components.countdown;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

/**
 * Defines a count down animation to be shown on a {@link TextView }.
 *
 */
public class CountDownView {

  private final TextView textView;
  private Animation animation;
  private int startCount;
  private int currentCount;
  private CountDownListener listener;

  private final Handler handler = new Handler();

  private final Runnable mCountDown = new Runnable() {
    @SuppressLint("DefaultLocale")
    public void run() {
      if (currentCount > 0) {
        textView.setText(String.format("%d", currentCount));
        textView.startAnimation(animation);
        currentCount--;
      } else {
        textView.setVisibility(View.GONE);
        if (listener != null)
          listener.onCountDownEnd(CountDownView.this);
      }
    }
  };

  /**
   * <p>
   * Creates a count down animation in the <var>textView</var>, starting from
   * <var>startCount</var>.
   * </p>
   * <p>
   * By default, the class defines a fade out animation, which uses
   * {@link AlphaAnimation } from 1 to 0.
   * </p>
   *
   * @param textView
   *            The view where the count down will be shown
   * @param startCount
   *            The starting count number
   */
  public CountDownView(TextView textView, int startCount) {
    this.textView = textView;
    this.startCount = startCount;

    animation = new AlphaAnimation(1.0f, 0.0f);
    animation.setDuration(1000);
  }

  /**
   * Starts the count down animation.
   */
  public void start() {
    handler.removeCallbacks(mCountDown);

    textView.setText(String.valueOf(startCount));
    textView.setVisibility(View.VISIBLE);

    currentCount = startCount;

    handler.post(mCountDown);
    for (int i = 1; i <= startCount; i++) {
      handler.postDelayed(mCountDown, i * 1000L);
    }
  }

  /**
   * Cancels the count down animation.
   */
  public void cancel() {
    handler.removeCallbacks(mCountDown);

    textView.setText("");
    textView.setVisibility(View.GONE);
  }

  /**
   * Sets the animation used during the count down. If the duration of the
   * animation for each number is not set, one second will be defined.
   */
  public void setAnimation(Animation animation) {
    this.animation = animation;
    if (this.animation.getDuration() == 0)
      this.animation.setDuration(1000);
  }

  /**
   * Returns the animation used during the count down.
   */
  public Animation getAnimation() {
    return animation;
  }

  /**
   * Sets a new starting count number for the count down animation.
   *
   * @param startCount
   *            The starting count number
   */
  public void setStartCount(int startCount) {
    this.startCount = startCount;
  }

  /**
   * Returns the starting count number for the count down animation.
   */
  public int getStartCount() {
    return startCount;
  }

  /**
   * Binds a listener to this count down animation. The count down listener is
   * notified of events such as the end of the animation.
   *
   * @param listener
   *            The count down listener to be notified
   */
  public void setCountDownListener(CountDownListener listener) {
    this.listener = listener;
  }

  /**
   * A count down listener receives notifications from a count down animation.
   * Notifications indicate count down animation related events, such as the
   * end of the animation.
   */
  public static interface CountDownListener {
    /**
     * Notifies the end of the count down animation.
     *
     * @param animation
     *            The count down animation which reached its end.
     */
    void onCountDownEnd(CountDownView animation);
  }
}
