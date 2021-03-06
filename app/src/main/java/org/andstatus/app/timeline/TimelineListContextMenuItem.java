package org.andstatus.app.timeline;
/*
 * Copyright (c) 2016 yvolk (Yuri Volkov), http://yurivolkov.com
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

import android.view.Menu;

import org.andstatus.app.ContextMenuItem;
import org.andstatus.app.WhichPage;
import org.andstatus.app.context.MyPreferences;
import org.andstatus.app.msg.TimelineActivity;
import org.andstatus.app.service.CommandData;
import org.andstatus.app.service.CommandEnum;
import org.andstatus.app.service.MyServiceManager;

public enum TimelineListContextMenuItem implements ContextMenuItem {
    SHOW_MESSAGES() {
        @Override
        public boolean execute(TimelineListContextMenu menu, TimelineListViewItem viewItem) {
            TimelineActivity.startForTimeline(menu.getActivity().getMyContext(), menu.getActivity(),
                    viewItem.timeline, null, false);
            return true;
        }
    },
    SYNC_NOW() {
        @Override
        public boolean execute(TimelineListContextMenu menu, TimelineListViewItem viewItem) {
            MyServiceManager.sendManualForegroundCommand(
                    CommandData.newTimelineCommand(CommandEnum.GET_TIMELINE, viewItem.timeline));
            return true;
        }
    },
    DELETE() {
        @Override
        public boolean execute(TimelineListContextMenu menu, TimelineListViewItem viewItem) {
            menu.getActivity().getMyContext().persistentTimelines().delete(viewItem.timeline);
            menu.getActivity().showList(WhichPage.CURRENT);
            return true;
        }
    },
    MAKE_DEFAULT() {
        @Override
        public boolean execute(TimelineListContextMenu menu, TimelineListViewItem viewItem) {
            MyPreferences.setDefaultTimelineId(viewItem.timeline.getId());
            menu.getActivity().showList(WhichPage.CURRENT);
            return true;
        }
    },
    UNKNOWN();

    @Override
    public int getId() {
        return Menu.FIRST + ordinal() + 1;
    }

    public static TimelineListContextMenuItem fromId(int id) {
        for (TimelineListContextMenuItem item : TimelineListContextMenuItem.values()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return UNKNOWN;
    }

    public void addTo(Menu menu, int order, int titleRes) {
        menu.add(Menu.NONE, this.getId(), order, titleRes);
    }

    public boolean execute(TimelineListContextMenu menu, TimelineListViewItem viewItem) {
        return false;
    }
}
