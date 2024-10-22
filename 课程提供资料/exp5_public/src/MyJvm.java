import java.util.List;

public class MyJvm {
    private static final int DEFAULT_CAPACITY = 16;
    private final JvmHeap heap;

    MyJvm() {
        heap = new JvmHeap(DEFAULT_CAPACITY);
    }

    // [4] 依据提示，完整书写以下方法的JML
    /*@ public normal_behavior
      @ assignable [4-1]; 写出调用方法前后，状态发生变化的字段 heap
      @ requires [4-2]; 条件分支1：加入count数量的object，size未达到上限
      @ ensures [4-3]; 调用方法后，size应满足的条件
      @ ensures [4-4]; 调用方法后，elements数组应满足仍包含原elements数组全部元素的条件
      @ also
      @ requires [4-5]; 条件分支2：加入count数量的object，size已达到上限
      @ ensures [4-6]; 调用方法后，size应满足的条件
      @ ensures [4-7]; 调用方法后，elements数组应满足: (1)若原elements数组中元素仍被引用，则该元素应包含于elements数组
      @ ensures [4-8]; 调用方法后，elements数组应满足: (2)若原elements数组中元素未被引用，则该元素应不包含于elements数组
      @*/

    /*@ public normal_behavior
      @ assignable elements, capacity, size;
      @ ensures capacity >= size;
      @ ensures size == \old(size) + 1;
      @ ensures (\exists int i; 0 < i && i <= size; elements[i].equals(newElement));
      @ ensures (\forall int i; 0 < i && i <= \old(size);
      @           (\exists int j; 0 < j && j <= size; elements[j].equals(\old(elements[i]))));
      @*/
    public void createObject(int count) {
        for (int i = 0; i < count; i++) {
            MyObject newObject = new MyObject();
            heap.add(newObject);
            if (heap.getSize() == DEFAULT_CAPACITY) {
                System.out.println("Heap reaches its capacity,triggered Garbage Collection.");
                GC();
            }
        }
    }

    public void setUnreferenced(List<Integer> objectId) {
        heap.setUnreferencedId(objectId);
    }

    public void GC() {
        heap.removeUnreferenced(); //1
    }

    public void getSnapShot() {
        System.out.println("Heap: " + heap.getSize());
        for (int i = 1; i <= heap.getSize(); i++) {
            MyObject mo = (MyObject) heap.getElementData()[i];
            System.out.print(mo.getId() + " ");
        }
        System.out.println("");
        MyObject youngest = heap.getYoungestOne();
        if (youngest != null) {
            System.out.print("the youngest one's id is " + youngest.getId());
        }
        System.out.println("");
        System.out.println("\n---------------------------------");
    }
}
