/*
 * Copyright 2018 CMPUT301W18T18
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ca.ualberta.cs.wrkify;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class ExclusiveExpandableListView extends ExpandableListView{
    protected int color_headerDefault;
    protected int color_headerSelected;
    protected int color_child;

    protected int currentGroup = -1;

    protected void setCurrentGroup(int groupNo){
        this.currentGroup = groupNo;
    }

    protected int getCurrentGroup(){
        return this.currentGroup;
    }

    public void notifyDeleting(int deleting){
        collapseGroup(deleting);
    }

    public void setColors(int color_headerDefault, int color_headerSelected, int color_child){
        this.color_headerDefault = color_headerDefault;
        this.color_headerSelected = color_headerSelected;
        this.color_child = color_child;
    }


    public int getDefaultHeaderColor(){
        return this.color_headerDefault;
    }

    public int getSelectedHeaderColor(){
        return this.color_headerSelected;
    }

    public int getChildColor(){
        return this.color_child;
    }




    public void init(){
        this.setOnGroupExpandListener(new OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int cur = getCurrentGroup();
                if (cur != groupPosition && cur != -1) {
                    collapseGroup(cur);
                }
                setCurrentGroup(groupPosition);
            }
        });

        this.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
    }


    public ExclusiveExpandableListView(Context context) {
        super(context);
        init();
    }

    public ExclusiveExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExclusiveExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


}
