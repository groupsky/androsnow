package eu.masconsult.android.androsnow;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class SnowView extends View {

	private Bitmap bitmap;

    /**
     * Everyone needs a little randomness in their life
     */
    private static final Random RNG = new Random();
    
    /**
     * Create a simple handler that we can use to cause animation to happen.  We
     * set ourselves as a target and we can use the sleep()
     * function to cause an update/invalidate to occur at a later date.
     */
    private RefreshHandler mRedrawHandler = new RefreshHandler();
    
    private ArrayList<Coordinate> snowFlakes = new ArrayList<Coordinate>(100);

    class RefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            SnowView.this.update();
            SnowView.this.invalidate();
        }

        public void sleep(long delayMillis) {
        	this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };
	
	public SnowView(Context context) {
		super(context);
	}
	
	public void update() {
		snowFlakes.add(new Coordinate(RNG.nextInt(getWidth()), -1));
		int i=0;
		while (i < snowFlakes.size()) {
			final Coordinate flake = snowFlakes.get(i);
			int diff = RNG.nextInt(3)-1;
			if (flake.x+diff < 0) diff = 0;
			if (flake.x+diff >= getWidth()) diff = 0;
			if (flake.y+1 >= getHeight() || bitmap.getPixel(flake.x+diff, flake.y+1) == Color.WHITE) {
				snowFlakes.remove(i);
				continue;
			}
			if (flake.y >= 0)
				bitmap.setPixel(flake.x, flake.y, Color.BLACK);
			flake.x += diff;
			flake.y++;
			bitmap.setPixel(flake.x, flake.y, Color.WHITE);
			i++;
		}
		mRedrawHandler.sleep(10);
	}

	public SnowView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public SnowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_4444);
		bitmap.eraseColor(Color.BLACK);
		update();
    }

	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		canvas.drawBitmap(bitmap, 0, 0, paint );
	}

    /**
     * Simple class containing two integer values and a comparison function.
     * There's probably something I should use instead, but this was quick and
     * easy to build.
     * 
     */
    private class Coordinate {
        public int x;
        public int y;

        public Coordinate(int newX, int newY) {
            x = newX;
            y = newY;
        }

        public boolean equals(Coordinate other) {
            if (x == other.x && y == other.y) {
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return "Coordinate: [" + x + "," + y + "]";
        }
    }
}
