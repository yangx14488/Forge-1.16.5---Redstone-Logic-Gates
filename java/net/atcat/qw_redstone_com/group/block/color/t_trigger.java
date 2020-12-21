/*××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××
 × The MIT License (MIT)
 × Copyright © 2020. 南织( 1448848683@qq.com )
 ×
 × Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 ×
 × The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 ×
 × THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ×××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××*/

package net.atcat.qw_redstone_com.group.block.color;

import net.atcat.qw_redstone_com.group.block.prototype;
import net.minecraft.block.BlockState;

public class t_trigger extends prototype_color {
    protected int getMultiplier ( BlockState state, int tintIndex ) {
        if ( state.has( prototype.OUTPUT_POWER ) && state.has( prototype.INPUT_POWER ) ) {
            int in = state.get( prototype.INPUT_POWER ) ;
            int out = state.get( prototype.OUTPUT_POWER ) ;
            return tintIndex == 1
                    ? ( out & 1 ) == 0
                        ? 15
                        : 0
                    : tintIndex == 2
                        ? ( out & 1 ) == 0
                            ? 0
                            : 15
                        : tintIndex == 3
                            ? ( in & 4 ) > 0
                                ? 15
                                : 0
                            : 0 ;
        } else {
            return 0 ;
        }
    } ;
}