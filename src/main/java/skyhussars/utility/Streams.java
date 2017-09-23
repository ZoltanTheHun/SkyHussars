/*
 * Copyright (c) 2017, ZoltanTheHun
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package skyhussars.utility;

import com.jme3.math.Vector3f;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Streams {

    public static <T> Stream<T> pf(Collection<T> obj, Predicate<? super T> predicate) {
        return obj.parallelStream().filter(predicate);
    }

    public static <T> void pp(Collection<T> obj, Consumer<? super T> action) {
        obj.parallelStream().forEach(action);
    }
    
    public static <T> void sp(Collection<T> obj, Consumer<? super T> action) {
        obj.stream().forEach(action);
    }
    
    public static <T,R> Stream<R> pm(Collection<T> obj, Function<? super T, ? extends R> mapper) {
        return obj.parallelStream().map(mapper);
    }
    
    public static <T,R> Stream<R> sm(Collection<T> obj, Function<? super T, ? extends R> mapper) {
        return  obj.parallelStream().map(mapper);
    }
    
    public static <T> List<T> list(Stream<T> obj){
        return obj.collect(Collectors.toList());
    }
    
    public static Vector3f sum(Stream<Vector3f> vectors){
        return vectors.reduce(Vector3f.ZERO,Vector3f::add);
    }
}
