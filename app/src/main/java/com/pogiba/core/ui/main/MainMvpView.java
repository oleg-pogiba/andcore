package com.pogiba.core.ui.main;

import java.util.List;

import com.pogiba.core.data.model.Ribot;
import com.pogiba.core.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError();

}
