import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import com.thehandsomecoder.cubix.R;

/**
 * Created with IntelliJ IDEA.
 * User: scottomalley
 * Date: 15/11/2013
 * Time: 12:36
 * To change this template use File | Settings | File Templates.
 */
public class NewMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.cube_grid_layout);

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new SquareImageViewAdapter(this));

    }
}
