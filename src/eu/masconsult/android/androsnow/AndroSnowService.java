package eu.masconsult.android.androsnow;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class AndroSnowService extends WallpaperService {

    private final Handler mHandler = new Handler();

    @Override
    public Engine onCreateEngine() {
        return new CubeEngine();
    }
    
    class CubeEngine extends Engine {

        private Bitmap bitmap;

        /**
         * Everyone needs a little randomness in their life
         */
        private final Random RNG = new Random();
        
        private ArrayList<Coordinate> snowFlakes = new ArrayList<Coordinate>(100);
        
        private boolean mVisible;

        private final Runnable mRefreshSnow = new Runnable() {
            public void run() {
                drawSnow();
            }
        };

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mRefreshSnow);
        }
        
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
        	bitmap = Bitmap.createBitmap(getApplicationContext().getWallpaperDesiredMinimumWidth(), getApplicationContext().getWallpaperDesiredMinimumHeight(), Config.ARGB_4444);
    		bitmap.eraseColor(Color.BLACK);
        	super.onCreate(surfaceHolder);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawSnow();
            } else {
                mHandler.removeCallbacks(mRefreshSnow);
            }
        }
        
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mRefreshSnow);
        }
        
        @Override
        public void onDesiredSizeChanged(int desiredWidth, int desiredHeight) {
        	super.onDesiredSizeChanged(desiredWidth, desiredHeight);
        	snowFlakes.clear();
        	bitmap = Bitmap.createBitmap(desiredWidth, desiredHeight, Config.ARGB_4444);
    		bitmap.eraseColor(Color.RED);
    		System.out.println("asdadasdadasdasdadasssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
        }

        void drawSnow() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    update(c);
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mRefreshSnow);
            if (mVisible) {
                mHandler.postDelayed(mRefreshSnow, 10);
            }
        }

        void update(Canvas c) {
        	
        	snowFlakes.add(new Coordinate(RNG.nextInt(c.getWidth()), -1));
    		int i=0;
    		while (i < snowFlakes.size()) {
    			final Coordinate flake = snowFlakes.get(i);
    			int diff = RNG.nextInt(3)-1;
    			if (flake.x+diff < 0) diff = 0;
    			if (flake.x+diff >= c.getWidth()) diff = 0;
    			if (flake.y+1 >= c.getHeight() || bitmap.getPixel(flake.x+diff, flake.y+1) == Color.WHITE) {
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
    		
    		c.drawBitmap(bitmap, 0, 0, new Paint());
        }
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
