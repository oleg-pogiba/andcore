package com.pogiba.core.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.pogiba.core.R;
import com.pogiba.core.data.SyncService;
import com.pogiba.core.data.model.Ribot;
import com.pogiba.core.ui.base.BaseActivity;
import com.pogiba.core.ui.base.FirebaseManager;
import com.pogiba.core.util.DialogFactory;

public class MainActivity extends BaseActivity implements MainMvpView {

  private static final String EXTRA_TRIGGER_SYNC_FLAG =
    "com.pogiba.core.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

  @Inject
  MainPresenter mMainPresenter;

  @Inject
  RibotsAdapter mRibotsAdapter;

  @Inject
  FirebaseManager firebaseManager;

  @BindView(R.id.recycler_view)
  RecyclerView mRecyclerView;

  /**
   * Return an Intent to start this Activity.
   * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
   * only be set to false during testing.
   */
  public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activityComponent().inject(this);
    // if user is signed out navigate to login activity
    checkAccess();
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle(getString(R.string.app_name));
    setSupportActionBar(toolbar);

    mRecyclerView.setAdapter(mRibotsAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mMainPresenter.attachView(this);
    //firebaseManager.addAuthStateListener();
    mMainPresenter.loadRibots();

    if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
      startService(SyncService.getStartIntent(this));
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

    //firebaseManager.removeAuthStateListener();

    mMainPresenter.detachView();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // action with ID action_refresh was selected
      case R.id.action_sign_out:
        firebaseManager.signOut();
        break;
      case R.id.action_settings:
        firebaseManager.removeAuthStateListener();
        getNavigator().navigateToProfile(this);
        break;
      default:
        break;
    }
    return true;
  }

  /***** MVP View methods implementation *****/

  @Override
  public void showRibots(List<Ribot> ribots) {
    mRibotsAdapter.setRibots(ribots);
    mRibotsAdapter.notifyDataSetChanged();
  }

  @Override
  public void showError() {
    DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_ribots))
      .show();
  }

  @Override
  public void showRibotsEmpty() {
    mRibotsAdapter.setRibots(Collections.<Ribot>emptyList());
    mRibotsAdapter.notifyDataSetChanged();
    Toast.makeText(this, R.string.empty_ribots, Toast.LENGTH_LONG).show();
  }

}
