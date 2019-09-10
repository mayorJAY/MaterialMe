/*
 * Copyright (C) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.materialme;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

/***
 * Main Activity for the Material Me app, a mock sports news application with poor design choices
 */
public class MainActivity extends AppCompatActivity{

    private ArrayList<Sport> mSportsData;
    private SportsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private final String RECYCLER_POSITION_KEY = "recycler_position";
    private int mPosition = RecyclerView.NO_POSITION;
    private LinearLayoutManager mLayoutManager;
    private static Bundle mBundleState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize the RecyclerView
        //Member variables
        mRecyclerView = findViewById(R.id.recyclerView);
        //Set the Layout Manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(this.mLayoutManager);
        //Initialize the ArrayLIst that will contain the data
        mSportsData = new ArrayList<>();
        //Initialize the adapter and set it ot the RecyclerView
        mAdapter = new SportsAdapter(this, mSportsData);
        mRecyclerView.setAdapter(mAdapter);
        //Get the data
        initializeData();
        //Enables moving and swiping of items on the list of Cards
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(mSportsData, from, to);
                mAdapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mSportsData.remove(viewHolder.getAdapterPosition());
                mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * Method for initializing the sports data from resources
     */
    private void initializeData() {
        //Get the resources from the XML file
        String[] mSportsList = getResources().getStringArray(R.array.sports_titles);
        String[] mSportsHeading = getResources().getStringArray(R.array.sports_heading);
        String[] mSportsInfo = getResources().getStringArray(R.array.sports_info);
        TypedArray mSportsImageResources = getResources().obtainTypedArray(R.array.sport_images);
        //Clear the existing data (to avoid duplication)
        mSportsData.clear();
        //Create the ArrayList of Sports objects with the titles and information about each sport
        for(int i = 0; i< mSportsList.length; i++){
            mSportsData.add(new Sport(mSportsList[i], mSportsHeading[i], mSportsInfo[i], mSportsImageResources.getResourceId(i, 0)));
        }
        //Clean up the data in the typed array once the Sport data ArrayList has been created
        mSportsImageResources.recycle();
        //Notify the adapter of the change
        mAdapter.notifyDataSetChanged();
    }

    public void resetSports(View view) {
        initializeData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save RecyclerView state
        mBundleState = new Bundle();
        mPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
        mBundleState.putInt(RECYCLER_POSITION_KEY, mPosition);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore RecyclerView state
        if (mBundleState != null){
            mPosition = mBundleState.getInt(RECYCLER_POSITION_KEY);
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            // Scroll the RecyclerView to mPosition
            mRecyclerView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save RecyclerView state
        outState.putInt(RECYCLER_POSITION_KEY, mLayoutManager.findFirstCompletelyVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore RecyclerView state
        if (savedInstanceState.containsKey(RECYCLER_POSITION_KEY)){
            mPosition = savedInstanceState.getInt(RECYCLER_POSITION_KEY);
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            // Scroll the RecyclerView to mPosition
            mRecyclerView.smoothScrollToPosition(mPosition);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
