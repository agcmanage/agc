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
package org.one2oneeum.vm.trace;

import org.one2oneeum.vm.DataWord;
import org.one2oneeum.vm.program.listener.ProgramListenerAdaptor;

public class ProgramTraceListener extends ProgramListenerAdaptor {

    private final boolean enabled;
    private OpActions actions = new OpActions();

    public ProgramTraceListener(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void onMemoryExtend(int delta) {
        if (enabled) actions.addMemoryExtend(delta);
    }

    @Override
    public void onMemoryWrite(int address, byte[] data, int size) {
        if (enabled) actions.addMemoryWrite(address, data, size);
    }

    @Override
    public void onStackPop() {
        if (enabled) actions.addStackPop();
    }

    @Override
    public void onStackPush(DataWord value) {
        if (enabled) actions.addStackPush(value);
    }

    @Override
    public void onStackSwap(int from, int to) {
        if (enabled) actions.addStackSwap(from, to);
    }

    @Override
    public void onStoragePut(DataWord key, DataWord value) {
        if (enabled) {
            if (value.equals(DataWord.ZERO)) {
                actions.addStorageRemove(key);
            } else {
                actions.addStoragePut(key, value);
            }
        }
    }

    @Override
    public void onStorageClear() {
        if (enabled) actions.addStorageClear();
    }

    public OpActions resetActions() {
        OpActions actions = this.actions;
        this.actions = new OpActions();
        return actions;
    }
}
