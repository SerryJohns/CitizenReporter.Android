package org.codeforafrica.citizenreporterandroid.storyboard;

import android.graphics.drawable.AnimatedStateListDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;
import java.util.Date;
import java.util.List;
import org.codeforafrica.citizenreporterandroid.R;
import org.codeforafrica.citizenreporterandroid.app.Constants;
import org.json.JSONArray;

public class Storyboard extends AppCompatActivity implements StoryboardContract.View {
  private static final String TAG = Storyboard.class.getSimpleName();
  StoryboardContract.Presenter presenter;
  ParseObject activeStory;
  LayoutInflater inflater;

  @BindView(R.id.attachmentsLayout) LinearLayout attachmentsLayout;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_storyboard);
    ButterKnife.bind(Storyboard.this);
    presenter = new StoryboardPresenter(this);
    String action = getIntent().getAction();
    if (action.equals(Constants.ACTION_EDIT_VIEW_STORY)) {
      String storyID = getIntent().getStringExtra("STORY_ID");
      presenter.openSavedStory(storyID);
    } else {
      String assignmentID = getIntent().getStringExtra("assignmentID");
      presenter.createNewStory(assignmentID);
    }

  }

  @Override protected void onStart() {
    super.onStart();
  }

  @Override protected void onStop() {
    super.onStop();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    presenter.saveStory(activeStory);
  }

  @Override public void showLoading() {

  }

  @Override public void hideLoading() {

  }

  @Override public void showUploadingProgress() {

  }

  @Override public void loadSavedReport(ParseObject story) {
    activeStory = story;
    String title = story.getString("title");
    String summary = story.getString("summary");
    String whoIsInvolved = story.getString("who");
    Date whenItOccurred = story.getDate("when");
    String location = story.getString("location");
    JSONArray media = story.getJSONArray("media");

    // set text to appropriate views

    presenter.loadAttachments(media);

  }

  @Override public void loadNewReport(ParseObject story) {
    Log.d(TAG, "loadNewReport: Log new report " + story.getObjectId());
    activeStory = story;

  }

  @Override public void showStoryNotFoundError(String message) {

  }

  @Override public void displayAttachments(List<ParseFile> files) {
    // get list of files pass them to the attachments adapter

  }

  @Override public void attachImage(ParseFile file) {
    View view = inflater.inflate(R.layout.item_image, null);
    TextView filename = (TextView) view.findViewById(R.id.image_filename_tv);
    ImageView image = (ImageView) view.findViewById(R.id.attached_image);

    filename.setText(file.getName());
    Picasso.with(Storyboard.this).load(file.getUrl()).into(image);
    attachmentsLayout.addView(view);
  }

  @Override public void attachVideo(ParseFile file) {
    View view = inflater.inflate(R.layout.item_video, null);
    TextView filename = (TextView) view.findViewById(R.id.video_filename_tv);

    filename.setText(file.getName());

    attachmentsLayout.addView(view);
  }

  @Override public void attachAudio(ParseFile file) {
    View view = inflater.inflate(R.layout.item_audio, null);
    TextView filename = (TextView) view.findViewById(R.id.audio_filename_tv);

    filename.setText(file.getName());

    attachmentsLayout.addView(view);
  }
}