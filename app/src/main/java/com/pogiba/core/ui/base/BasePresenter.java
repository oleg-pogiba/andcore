package com.pogiba.core.ui.base;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getView().
 */
public class BasePresenter<T extends MvpView> implements Presenter<T> {

  private T view;

  @Override
  public void attachView(T view) {
    this.view = view;
  }

  @Override
  public void detachView() {
    view = null;
  }

  public boolean isViewAttached() {
    return view != null;
  }

  public T getView() {
    return view;
  }

  public void checkViewAttached() {
    if (!isViewAttached()) throw new MvpViewNotAttachedException();
  }

  public static class MvpViewNotAttachedException extends RuntimeException {
    public MvpViewNotAttachedException() {
      super("Please call Presenter.attachView(MvpView) before" +
              " requesting data to the Presenter");
    }
  }
}

