/*
 * Copyright (c) [2016] [ <one2one.camp> ]
 * This file is part of the one2oneeumJ library.
 *
 * The one2oneeumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The one2oneeumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the one2oneeumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.one2oneeum.vm.program;

import org.one2oneeum.vm.DataWord;
import org.one2oneeum.vm.program.listener.ProgramListener;
import org.one2oneeum.vm.program.listener.ProgramListenerAware;

public class Stack extends java.util.Stack<DataWord> implements ProgramListenerAware {

    private ProgramListener programListener;

    @Override
    public void setProgramListener(ProgramListener listener) {
        this.programListener = listener;
    }

    @Override
    public synchronized DataWord pop() {
        if (programListener != null) programListener.onStackPop();
        return super.pop();
    }

    @Override
    public DataWord push(DataWord item) {
        if (programListener != null) programListener.onStackPush(item);
        return super.push(item);
    }

    public void swap(int from, int to) {
        if (isAccessible(from) && isAccessible(to) && (from != to)) {
            if (programListener != null) programListener.onStackSwap(from, to);
            DataWord tmp = get(from);
            set(from, set(to, tmp));
        }
    }

    private boolean isAccessible(int from) {
        return from >= 0 && from < size();
    }
}
