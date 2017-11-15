package skyhussars.utility;

import java.util.List;

public class Collections {
    
    public static class ZList<T> {
        private List<T> list;
        private ZList(List<T> list){
            this.list = list;
        }
        
        public ZList add(T elem){
            list.add(elem);
            return this;
        }
        
        public List<T> unwrap(){
            return list;
        }
    }
    public static <T> ZList zList(List<T> list){
        return new ZList(list);
    }
}
