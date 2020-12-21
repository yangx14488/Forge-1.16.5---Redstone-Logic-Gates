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

public class xor_step_inv extends prototype {

    public xor_step_inv( ) {
        super( io_2|io_1, io_0|io_3) ; // 1|2|4|8
    } ;

    protected int calc_io_output( int power_in ) {

        int in_1 = power_in & io_1 ;
        int in_2 = power_in & io_2 ;

        return ( in_1 > 0 || in_2 > 0 ) ? ( in_1 == 0 ) != ( in_2 == 0 ) ? 9 : 8 : 0 ;

    } ;

}
