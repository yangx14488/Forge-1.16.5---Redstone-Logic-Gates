/*××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××
 × The MIT License (MIT)
 × Copyright © 2020. 南织( 1448848683@qq.com )
 ×
 × Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software” ), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 ×
 × The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 ×
 × THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ×××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××××*/

package net.atcat.qw_redstone_com.group.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.RepeaterBlock;

public class rs_latch extends prototype {

    private static final int _output = io_0|io_2 ;

    public rs_latch( ) {
        super( io_1|io_3, _output ) ;
    } ;

    protected int calc_io_output( int power_in ) {
        return 0 ;
    } ;
    @Override
    protected int calc_io_output( BlockState state, int flag_in, int flag_out, int calc_in, boolean isTick, boolean isUpdate, boolean isPlace ) {

        int out_0 = ( calc_in & io_1 ) > 0 ? io_0 : 0 ;
        int out_2 = ( calc_in & io_3 ) > 0 ? io_2 : 0 ;

        return ( out_0 | out_2 ) > 0
                ? out_0 | out_2
                : flag_out == _output || flag_out == 0
                    ? io_0
                    : flag_out ;
    } ;

}
