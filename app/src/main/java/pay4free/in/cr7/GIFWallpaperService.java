package pay4free.in.cr7;

import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by AAKASH on 27-10-2017.
 */

public class GIFWallpaperService extends WallpaperService {
    @Override
    public WallpaperService.Engine onCreateEngine() {
        try {
            Movie movie = Movie.decodeStream(getResources().getAssets().open("ronaldogoad.gif"));
            return new GIFWallpaperEngine(movie);
        } catch (IOException e) {

            return null;
        }
    }
    private class GIFWallpaperEngine extends WallpaperService.Engine
    {
      private final int frameDuration=20;
        private SurfaceHolder holder;
        private Movie movie;
        private boolean visible;
        private Handler handler;

        public GIFWallpaperEngine(Movie movie) {
            this.movie = movie;
            handler=new Handler();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.holder=surfaceHolder;
        }
        private Runnable drawgif=new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        private void draw() {
            if(visible)
            {
                Canvas canvas=holder.lockCanvas();
                canvas.save();
                canvas.scale(4f,4f);
                movie.draw(canvas,-100,0);
                canvas.restore();
                holder.unlockCanvasAndPost(canvas);
                movie.setTime((int)System.currentTimeMillis()%movie.duration());


                handler.removeCallbacks(drawgif);
                handler.postDelayed(drawgif,frameDuration);
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible=visible;
            if(visible)
            {
                handler.post(drawgif);

            }
            else
            {
                handler.removeCallbacks(drawgif);
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawgif);
        }
    }
}
