package com.elegion.tracktor.ui.results;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResultsViewModelTest {

    @Mock
    private RealmRepository mRepository;

    @Mock
    private Observer<List<Track>> observer;

    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();

    @Rule
    public TestRule mTestRule = new InstantTaskExecutorRule();

    private ResultsViewModel mViewModel;

    private List<Track> mTracks;

    @Before
    public void setUp() throws Exception {
        mTracks = new ArrayList<>();
        mTracks.add(new Track());
        when(mRepository.getAll()).thenReturn(mTracks);
        mViewModel = new ResultsViewModel(mRepository);
        mViewModel.getTracks().observeForever(observer);
    }

    @Test
    public void checkObserverTriggeredOnChangedList(){
        mViewModel.loadTracks();
        verify(observer, times(1)).onChanged(mTracks);
    }
}