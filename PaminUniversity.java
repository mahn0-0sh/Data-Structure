
import java.util.Scanner;

public class Main {
    List<Student> std_list = new List<>();
    List<Course> crs_list = new List<>();
    List<Grade> grd_list = new List<>();
    GList<Student> stu_grd_list = new GList<>();
    GList<Course> crs_grd_list = new GList<>();
    AVLTree<String> std_names = new AVLTree<>();
    AVLTree<String> crs_names = new AVLTree<>();
    int a,b,p;
    HashTable std_codes;
    HashTable crs_codes;
    Graph relevance_graph = new Graph();
    Graph compare_graph = new Graph();
//    Node<Course>[] course_array;
    Course[] course_array;
    double[][] desire_matrix;

    //ADD
    void ADDS(int code, String name) {
        Student student = new Student(code, name);
        std_list.addLast(student);
        stu_grd_list.add(student);

        if(p!=1) {
            Node<Student> sNode = stu_grd_list.tail.next;
            std_names.insert(name, sNode, null);
            std_codes.put(code, sNode);
        }

    }

    void ADDC(int code, String name) {
        Course course = new Course(code, name);
        crs_list.addLast(course);
        crs_grd_list.add(course);

        if(p!=1) {
            Node<Course> sNode = crs_grd_list.tail.next;
            crs_names.insert(name, null, sNode);
            crs_codes.put(code, sNode);
        }
    }

    void ADDG(int s_code, int c_code, int sem_code, double grade) {
        Grade grd = new Grade(s_code, c_code, grade, sem_code);
        List<Grade> list =  stu_grd_list.find_s(s_code);
        List<Grade> clist =  crs_grd_list.find_c(c_code);
        list.addLast(grd);
        clist.addLast(grd);
    }



    //EDIT
    void EDITS(int code, String name) {
        Student std = std_list.find(code);
        if(p!=1) {
            std_names.edit(std.name, name);
        }
        std.name = name;
    }

    void EDITC(int code, String name) {
        Course crs = crs_list.find(code);
        if(p!=1) {
            crs_names.edit(crs.name, name);
        }
        crs.name = name;
    }

    void EDITG(int s_code, int c_code, double grd) {
        stu_grd_list.find_s(s_code).edit_grade_crs(c_code, grd);
        crs_grd_list.find_c(c_code).edit_grade_std(s_code, grd);
    }



    //DELETE
    void DELETES(int code) {
        Student student = std_list.find(code);
        std_list.remove(student);
        if(p!=1) {
            std_names.delete(student.name);
            std_codes.remove(code);
        }
        List gList=stu_grd_list.find_s(code);
        stu_grd_list.delete_node(code, true);
        delete_grades_of_s(gList, code);
    }

    void DELETEC(int code) {
        Course course = crs_list.find(code);
        crs_list.remove(course);
        if(p!=1) {
            crs_names.delete(course.name);
            crs_codes.remove(code);
        }
        List gList = crs_grd_list.find_c(course.code);
        crs_grd_list.delete_node(code, false);
        delete_grades_of_c(gList, code);
    }

    void DELETEG(int s_code, int c_code) {
        crs_grd_list.find_c(c_code).remove_grade(s_code, c_code);
        stu_grd_list.find_s(s_code).remove_grade(s_code, c_code);
    }


    void delete_grades_of_s(List grd_list, int code) {
        LNode node = grd_list.head;
        while ( node.next != null ) {
            Grade grade = (Grade) node.next.value;
            List list = crs_grd_list.find_c(grade.c_code);
            list.remove(grade);
            node = node.next;
        }
    }

    void delete_grades_of_c(List grd_list, int code) {
        LNode node = grd_list.head;
        while ( node.next != null ) {
            Grade grade = (Grade) node.next.value;
            List list = stu_grd_list.find_s(grade.s_code);
            list.remove(grade);
            node = node.next;
        }
    }


    //NUMBER
    int NUMBERC (int code) {
        try {
            return stu_grd_list.find_s(code).size;
        } catch (Exception e) {
            return 0;
        }
    }

    int NUMBERS (int code) {
        try {
            return crs_grd_list.find_c(code).size;
        } catch (Exception e) {
            return 0;
        }
    }


    void search_prints(Node<Student> student_node, Student student) {
        int s = student_node.right.size;
        System.out.println(student.code+" "+student.name+" "+s);
        List gList = student_node.right;
        LNode node = gList.head.next;
        prt(node, true);
    }
    void search_printc(Node<Course> courseNode, Course course) {
        int s = courseNode.right.size;
        System.out.println(course.code+" "+course.name+" "+s);
        List gList = courseNode.right;
        LNode node = gList.head.next;
        prt(node, false);
    }
    void prt(LNode node, boolean bl) {
        while (node!=null) {
            Grade grade = (Grade) node.value;
            double grd = grade.grade;
            if(bl) {
                System.out.print(grade.c_code+" "+grade.sem_code+" ");
            } else {
                System.out.print(grade.s_code+" "+grade.sem_code+" ");
            }
            if(grd % 1 == 0) System.out.println((int) grd);
            else System.out.println(grd);

            node = node.next;
        }
    }

    //SEARCH
    void SEARCHSN(String name) {
        Node<Student> node = std_names.find(name).stuNode;
        search_prints(node, node.data);
    }

    void SEARCHCN(String name) {
        Node<Course> node = crs_names.find(name).crsNode;
        search_printc(node, node.data);
    }

    void SEARCHSC(int code) {
        Node<Student> node = (Main.Node<Main.Student>) std_codes.get(code);
        System.out.println(std_codes.hash(code));
        search_prints(node, node.data);
    }

    void SEARCHCC(int code) {
        Node<Course> node = (Main.Node<Main.Course>) crs_codes.get(code);
        System.out.println(crs_codes.hash(code));
        search_printc(node, node.data);
    }


    void ISRELATIVE(int c1, int c2) {
        if(relevance_graph.has_path(c1, c2)) System.out.println("yes");
        else System.out.println("no");
    }

    void ALLRELATIVE(int code) {
        print_relatives(code);
        System.out.println();
    }

    private void COMPARE(int code1, int code2) {
        boolean a_to_b = compare_graph.has_path(code1, code2);
        boolean b_to_a = compare_graph.has_path(code2, code1);
        if(a_to_b && !b_to_a) System.out.println(">");
        else if(!a_to_b && b_to_a) System.out.println("<");
        else System.out.println("?");
    }

    void print_relatives(int code) {
        LNode<Course> node = crs_list.head.next;
        List<Integer> codes = new List<>();
        while (node!=null) {
            int c = node.value.code;
            if(c != code && relevance_graph.has_path(code, c)) {
                codes.addLast(c);
            }
            node = node.next;
        }
        int[] cds = array(codes);
        merge_sort(cds, cds.length);
        print(cds);
    }

    void print(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]+" ");
        }
    }

    int[] array(List<Integer> list) {
        int size = list.size;
        int[] array = new int[size];
        LNode<Integer> node = list.head.next;
        int i = 0;
        while (node!=null) {
            array[i] = node.value;
            node = node.next;
            i++;
        }
        return array;
    }


    static void merge_sort(int[] a, int n) {
        if (n<2) {
            return;
        }
        int mid = n/2;
        int[] l=new int[mid];
        int[] r=new int[n-mid];

        for (int i = 0; i < mid; i++) {
            l[i] = a[i];
        }
        for (int i = mid; i < n; i++) {
            r[i-mid] = a[i];
        }
        merge_sort(l, mid);
        merge_sort(r, n-mid);
        merge(a, l, r, mid, n-mid);
    }

    public static void merge(int[] a, int[] l, int[] r, int left, int right) {
        int i=0, j=0, k=0;
        while (i<left && j<right) {
            if (l[i] <= r[j]) a[k++] = l[i++];
            else a[k++] = r[j++];
        }
        while (i<left) {
            a[k++] = l[i++];
        }
        while (j<right) {
            a[k++] = r[j++];
        }
    }

    void draw_dir_graph() {
        Node<Student> node = stu_grd_list.head.next;
        int index = 0;

        while (node!=null) {
            Vertex curr = new Vertex(node.data.code, node, null);
            LNode<Vertex> v = compare_graph.vertexs.head.next;
            while (v!=null) {
                if(is_dir_connected(v.value.student_pointer, curr.student_pointer)) compare_graph.draw_dir_edge(v.value, curr);
                else if(is_dir_connected(curr.student_pointer, v.value.student_pointer)) compare_graph.draw_dir_edge(curr, v.value);
                v = v.next;
            }
            curr.index = index;
            compare_graph.add_vertex(curr);
            index++;
            node = node.next;
        }
    }

    void draw_graph() {
        Node<Course> node = crs_grd_list.head.next;
        int index = 0;

        while (node!=null) {
            Vertex curr = new Vertex(node.data.code, null, node);
            LNode<Vertex> v = relevance_graph.vertexs.head.next;
            while (v!=null) {
                if(is_connected(v.value.course_pointer, curr.course_pointer)) relevance_graph.draw_edge(v.value, curr);
                v = v.next;
            }
            curr.index = index;
            relevance_graph.add_vertex(curr);
            index++;
            node = node.next;
        }
    }

    boolean is_connected(Node<Course> c1, Node<Course> c2) {
        List<Grade> gradeList1 = c1.right;
        List<Grade> gradeList2 = c2.right;
        return are_similar(gradeList1, gradeList2);
    }

    boolean is_dir_connected(Node<Student> s1, Node<Student> s2) {
        List<Grade> gradeList1 = s1.right;
        List<Grade> gradeList2 = s2.right;
        return is_better(gradeList1, gradeList2);
    }

    boolean is_better(List<Grade> gList1, List<Grade> gList2) {
        int sim = 0;
        int better = 0;
        LNode<Grade> node1 = gList1.head.next;
        while (node1!=null) {
            LNode<Grade> node2 = gList2.head.next;
            while (node2!=null) {
                if(node1.value.c_code == node2.value.c_code) {
                    sim++;
                    if(node1.value.grade > node2.value.grade) better++;
                }
                node2 = node2.next;
            }
            node1 = node1.next;
        }
        if(better>sim/2) return true;
        return false;
    }

    boolean are_similar(List<Grade> gList1, List<Grade> gList2) {
        int count = 0;
        LNode<Grade> node1 = gList1.head.next;
        while (node1!=null) {
            LNode<Grade> node2 = gList2.head.next;
            while (node2!=null) {
                if(node1.value.s_code == node2.value.s_code) count++;
                node2 = node2.next;
            }
            node1 = node1.next;
        }

        int s1 = gList1.size;
        int s2 = gList2.size;
        if(count>s1/2 && count>s2/2) return true;
        return false;
    }



    //MAKE AN ARRAY OF COURSES NODES
    void make_array() {
        course_array = new Course[crs_list.size];
        LNode node = crs_list.head.next;
        int i = 0;
        while (node != null) {
            Course course = (Course) node.value;
            course.index = i;
            course_array[i++] = course;
            node = node.next;
        }
    }

    void complete_desire_matrix() {
        try {
            make_array();
        } catch (Exception e) {
            return;
        }

        int t = 90000;
        int l = course_array.length;
            desire_matrix = new double[l][t];
            for (int i = 0; i < course_array.length; i++) {
                for (int j = 0; j < t; j++) {
                    desire_matrix[i][j] = -1;
                }
            }


        Course course = new Course();
        List<Grade> gList = new List<>();
        LNode<Grade> gNode = new LNode();

            for (int i = 0; i < l; i++) {
                try {
                    course = course_array[i];
                    gList = crs_grd_list.find_c(course.code);
                    gNode = gList.head.next;
                    while (gNode != null) {
                        Grade grade = (Grade) gNode.value;
                        double k = desire_matrix[i][grade.sem_code - 10000];

                        if (k != -1) {
                            desire_matrix[i][grade.sem_code - 10000] += grade.grade;
                            desire_matrix[i][grade.sem_code - 10000] /= 2;
                        } else {
                            desire_matrix[i][grade.sem_code - 10000] = grade.grade;
                        }


                        gNode = gNode.next;
                    }
                } catch (Exception e) {
                    return;
                }

            }

            for (int i = 0; i < course_array.length; i++) {
                for (int j = 0; j < t; j++) {
                    if(desire_matrix[i][j] == -1) desire_matrix[i][j] = 0;
                }
            }

    }

    void MINRISK(int code) {
        List list = stu_grd_list.find_s(code);
        int size = list.size;
        min_risk(code, size, list);
    }

    int[][] compact_array(int[][] sem_num, int size) {
        int l=0;
        int c = sem_num[0].length;
        for (int i = 0; i < size; i++) {
            if(i<c && sem_num[0][i]==-1) {
                l=i;
                break;
            }
        }
        if(l==0) l=c;

        int[][] new_sem_num = new int[2][l];
        for (int i = 0; i < l; i++) {
            new_sem_num[0][i] = sem_num[0][i];
            new_sem_num[1][i] = sem_num[1][i];
        }
        return new_sem_num;
    }

    Semester[] make_array_of_sem(int[][] res, int size, int[][] sem_num) {
        sem_num = compact_array(sem_num, size);

        int l = sem_num[0].length;
        int[][] copy_sem_num = new int[2][l];
        copy_sem_num[0] = sem_num[0];
        equalize(copy_sem_num, sem_num, l);

        merge_sort(sem_num[0], l);
        Semester[] semesters = new Semester[l];
        for (int i = 0; i < l; i++) {
            int sem = sem_num[0][i];
            int num_of_sem = 0;
            for (int j = 0; j < l; j++) {
                if(copy_sem_num[0][j] == sem) {
                    num_of_sem = copy_sem_num[1][j];
                    break;
                }
            }
            int[] sem_courses = null;
            int k = 0;
            for (int j = 0; j < size; j++) {
            //    try {
                    if(res[1][j] == sem) {
                        if(res[0][j]<course_array.length) {
                            if (course_array[res[0][j]] != null) {
                                int a = course_array[res[0][j]].code;
                                sem_courses = append(sem_courses, a);
                            }
                        }
                    }
            }
            Semester semester = new Semester();
            semester.sem = sem;
            merge_sort(sem_courses, sem_courses.length);
            semester.courses = sem_courses;
            semesters[i] = (semester);
        }
        return semesters;
    }

    int[] append(int[] array, int new_data) {
        int[] new_array;
        if(array == null) {
            new_array = new int[1];
            new_array[0] = new_data;
        } else {
            int l = array.length;
            new_array = new int[l + 1];
            for (int i = 0; i < l; i++) {
                new_array[i] = array[i];
            }
            new_array[l] = new_data;
        }
        return new_array;
    }

    int find_course_by_index(int index) {
        LNode node = crs_list.head.next;
        int crs = 0;
        while (node!=null) {
            if( ((Course)(node.value)).index == index){
                crs = ((Course)(node.value)).code;
                break;
            }
            node = node.next;
        }
        return  crs;
    }
    void print(int[][] res, int size, int[][] sem_num, boolean bee) {
        if(!bee) sem_num = compact_array(sem_num, size);
        Semester[] semesters = make_array_of_sem(res, size, sem_num);
        int l = sem_num[0].length;

        for (int i = 0; i < l; i++) {
            semesters[i].print();
            System.out.println();
        }
    }

    void min_risk(int code, int size, List list) {
        // course index and semester
        int[][] crs_sem = courses_taken_by_stu(list);

        // score of taken courses
        double max = calculate_score(crs_sem, size);

        // semester and number of courses taken in this semester
        int[][] sem_num = sem_number(crs_sem, size);

        //for every permutation of semesters
        //assign to course's
        //calculate scores
        int[][] crs_perm = new int[2][size];
        int[][] result = new int[2][size];
        result[0] = crs_sem[0];
        result[1] = crs_sem[1];
        crs_perm[0] = crs_sem[0];

        Permutation permutation = new Permutation();
        List<List<Integer>> permutations = permutation.permute(sem_num, size);

        LNode node = permutations.head.next;

        boolean bee = false;
        while (node!=null) {
                int[] arraied = array((List<Integer>) node.value);
                crs_perm[1] = arraied;
                double s = calculate_score(crs_perm, size);

                if(s>max) {
                    equalize(result ,crs_perm, size);
                    max = s;
                }
                else if (s==max) {
                    bee = true;
                    sem_num = compact_array(sem_num, size);
                    Semester[] semesters = make_array_of_sem(result, size, sem_num);
                    Semester[] semesters2 = make_array_of_sem(crs_perm, size, sem_num);
                    for (int i = 0; i < semesters.length; i++) {
                        int[] crs = semesters[i].courses;
                        int[] crs2 = semesters2[i].courses;
                        for (int j = 0; j < crs.length; j++) {
                            if(crs[j]<crs2[j]) break;
                            else if (crs[j]>crs2[j]) {
                                equalize(result, crs_perm, size);
                                break;
                            }
                        }
                    }
                }
            node = node.next;
        }
        print(result, size, sem_num, bee);
    }

    void equalize(int[][] res, int[][] crs, int size) {
        for (int j = 0; j < size; j++) {
            res[1][j] = crs[1][j];
        }
    }

    void init_array(int[][] crs_sem) {
        for (int i = 0; i < crs_sem[0].length; i++) {
            crs_sem[0][i] = -1;
        }
    }

    int[][] sem_number(int[][] crs_sem , int size) {
        int[][] sem_number = new int[2][size];
        init_array(sem_number);
        boolean[] counted = new boolean[size];
        int k = 0;

        for (int i = 0; i < size; i++) {
            if(!counted[i]) {
                int count = 0;
                int sem_code = crs_sem[1][i];
                for (int j = i; j < size; j++) {
                    if(crs_sem[1][j] == sem_code) {
                        counted[j] =true;
                        count++;
                    }
                }
                sem_number[0][k] = sem_code;
                sem_number[1][k] = count;
                k++;
            }
        }
        return sem_number;
    }

    double calculate_score(int[][] crs_sem, int size) {
        double score = 0;
        for (int i = 0; i < size; i++) {
            double d = desire_matrix[ crs_sem[0][i] ][ crs_sem[1][i]-10000 ];
            score += d;
        }
        return score;
    }

    int[][] courses_taken_by_stu(List list) {
        int size = list.size;
        int[][] index_sem = new int[2][size];
        for (int i = 0; i < size; i++) {
            index_sem[0][i] = -1;
        }

        LNode node = list.head.next;
        while (node!=null) {
            Grade grade = (Grade) node.value;
            Course course = crs_list.find(grade.c_code);
            add(index_sem ,course.index, grade.sem_code, size);
            node = node.next;
        }
        return index_sem;
    }

    void add(int[][] ind_sem, int c_index, int sem, int size) {
        for (int i = 0; i < size; i++) {
            if(ind_sem[0][i] == -1) {
                ind_sem[0][i] = c_index;
                ind_sem[1][i] = sem;
                break;
            }
        }
    }







    class Student {
        int code;
        String name;
        public Student(int code, String name) {
            super();
            this.code = code;
            this.name = name;
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return name+" "+code;
        }
    }

    class Course {
        int code; // 5
        int index;
        String name;
        public Course(int code, String name) {
            super();
            this.code = code;
            this.name = name;
        }

        public Course() {

        }
    }

    class Grade {
        double grade;
        int sem_code;
        int c_code;
        int s_code;
        public Grade(int s_code, int c_code, double grade, int sem_code) {
            super();
            this.grade = grade;
            this.sem_code = sem_code;
            this.s_code = s_code;
            this.c_code = c_code;
        }

        @Override
        public String toString() {
            return s_code+" "+c_code+" "+grade;
        }
    }








    class List<T> {
        //fields
        int size = 0;
        LNode<T> head;
        LNode<T> tail;

        //init
        void init() {
            head = new LNode();
            tail = new LNode();
        }
        public List() {
            super();
            init();
        }


        //TODO
        //remove
        public void remove(T data) {
            LNode<T> prev = head;
            LNode<T> curr = head.next;
            while (curr != null) {
                if (curr.value.equals(data)) {
                    prev.next = (curr.next);
                    if(curr.equals(tail.next)) tail.next = prev;
                    break;
                }
                prev = curr;
                curr = curr.next;
            }
            //   }
            size--;
        }

        public void remove(LNode<T> data) {
            LNode node = head.next;
            while (!node.next.equals(data)) {
                node = node.next;
            }
            node.next = data.next;
            size--;
        }


        //insert
        void addFirst(T item) {
            LNode<T> node = new LNode(item, null);
            head.next = node;

            if(tail.next == null) {
                tail.next = node;
            }
            size++;
        }

        void addLast(T item) {
            if(head.next == null) {
                addFirst(item);
            } else {
                LNode<T> last = tail.next;
                LNode<T> node = new LNode(item, null);
                if(last != null) {
                    last.next = node;
                }

                tail.next = node;
                size++;
            }
        }


        //print
        void print() {
            LNode<T> node = head.next;
            int i = 5;
            while(node != null) {
                System.out.println(node.value.toString()+" ");
                node = node.next;
            }
        }




        //find
        T find(int code) {
            LNode node = head.next;
            while(node != null) {
                if(node.value instanceof Student) {
                    if( ((Main.Student)node.value).code == code ) {
                        return (T) node.value;
                    }
                }
                if(node.value instanceof Course) {
                    if( ((Main.Course)node.value).code == code ) {
                        return (T) node.value;
                    }
                }
                if(node.value instanceof Grade) {
                    if( ((Main.Grade)node.value).s_code == code ) {
                        return (T) node.value;
                    }
                }
                if(node.value instanceof Vertex) {
                    if(((Vertex)node.value).data == code) {
                        return (T) node.value;
                    }
                }
                node = node.next;
            }
            return null;
        }


        T find_by_s(int code) {
            LNode node = head.next;
            while(node != null) {
                if(node.value instanceof Grade) {
                    if( ((Main.Grade)node.value).s_code == code ) {
                        return (T) node.value;
                    }
                }
                node = node.next;
            }
            return null;
        }

        T find_by_c(int code) {
            LNode node = head.next;
            while(node != null) {
                if(node.value instanceof Grade) {
                    if( ((Main.Grade)node.value).c_code == code ) {
                        return (T) node.value;
                    }
                }
                node = node.next;
            }
            return null;
        }


        //edit
        void edit_grade_crs(int c_code, double grade) {
            Grade grd = (Main.Grade) find_by_c(c_code);
            grd.grade = grade;
        }

        void edit_grade_std(int s_code, double grade) {
            Grade grd = (Main.Grade) find_by_s(s_code);
            grd.grade = grade;
        }

        public void remove_grade(int s_code, int c_code) {
            LNode<T> node = head;
            while (node.next != null) {
                Grade grade = (Grade) node.next.value;
                if(grade.s_code == s_code && grade.c_code == c_code) {
                    remove((T) grade);
                    break;
                }
                node = node.next;
            }
        }

    }

    class LNode<T> {
        T value;
        LNode<T> next;

        public LNode(T value, LNode<T> next) {
            this.value = value;
            this.next = next;
        }

        public LNode() {
        }
    }





    class Node<T>{
        T data;
        List<Grade> right;
        Node<T> next;

        public Node(T value, Node<T> n) {
            this.data = value;
            this.next = n;
            right = new List<>();
        }

        public Node() {
        }

        @Override
        public String toString() {
            String string = "";
            LNode<Grade> node = right.head.next;
            while (node != null) {
                string += node.value.c_code+" "+node.value.grade;
                node = node.next;
            }
            return string;
        }
    }
    class GList<T> {
        int size = 0;
        Node<T> head;
        Node<T> tail;

        void init() {
            head = new Node();
            tail = new Node();
        }

        T findStu(int code) {
            Node n = head.next;
            while (n!=null) {
                Student student = (Student) n.data;
                if(student.code == code) return (T) n.data;
            }
            return null;
        }

        T findCrs(int code) {
            Node n = head.next;
            while (n!=null) {
                Course course = (Course) n.data;
                if(course.code == code) return (T) n.data;
            }
            return null;
        }
        public List<Main.Grade> find_c(int code) {
            Node node = head.next;
            while ( node!=null && ((Course) node.data).code != code ) {
                node = node.next;
            }
            return node.right;
        }

        public List<Main.Grade> find_s(int code) {
            Node node = head.next;
            while ( node!=null && ((Student) node.data).code != code ) {
                node = node.next;
            }
            return node.right;
        }

        void delete_node(int code, boolean b) {
            Node node = head;
            if(b) {
                while ( ((Student) node.next.data).code != (code)) {
                    node = node.next;
                }
            } else {
                while ( ((Course) node.next.data).code != (code)) {
                    node = node.next;
                }
            }
            if(tail.next.equals(node.next)) {
                tail.next = node;
            }
            node.next = node.next.next;
        }

        public GList() {
            super();
            init();
        }


        void add(T item) {
            if(head.next == null) {
                addFirst(item);
            } else {
                Node<T> last = tail.next;
                Node<T> node = new Node(item, null);
                if(last != null) {
                    last.next = node;
                }
                tail.next = node;
                size++;
            }
        }

        void addFirst(T item) {
            Node<T> node = new Node(item, head.next);
            head.next = node;

            if(tail.next == null || tail.next.equals(head)) {
                tail.next = node;
            }
            size++;
        }

    }




    public class AVLTree<String extends Comparable<String>> {
        private class Node<String extends Comparable<String>> {
            private String data;
            private Node<String> left_child;
            private Node<String> right_child;
            Main.Node<Student> stuNode;
            Main.Node<Course> crsNode;
            private int height = 1;

            public Node(String data, Main.Node<Student> s, Main.Node<Course> c) {
                this.data = data;
                this.stuNode = s;
                this.crsNode = c;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }
        }

        private Node<String> root;

        void insert(String data, Main.Node<Student> s, Main.Node<Course> c) {
            root = insert(data, root, s, c);
        }

        private Node<String> insert(String data, Node<String> node, Main.Node<Student> s, Main.Node<Course> c) {
            //Insert root
            if (node == null) {
                Node<String> root_node = new Node<>(data, s, c);
                return root_node;
            }
            if ( (data).compareTo(node.getData()) < 0 ) {
                node.left_child = (insert(data, node.left_child, s, c));
            } else {
                node.right_child = (insert(data, node.right_child, s, c));
            }
            update_height(node);
            return rotate(node);
        }



        public void delete(String data) {
            root = delete(data, root);
        }


        private Node<String> delete(String data, Node<String> node) {
            if (node == null) {
                return null;
            }
            if (data.compareTo(node.getData()) < 0) {
                node.left_child = (delete(data, node.left_child));
            } else if (data.compareTo(node.getData()) > 0) {
                node.right_child = (delete(data, node.right_child));
            } else {
                if (node.left_child == null) {
                    return node.right_child;
                } else if (node.right_child == null) {
                    return node.left_child;
                }

                if (node.left_child.height > node.right_child.height) {
                    Node nd = get_max(node.left_child);
                    node.setData((String) nd.data);
                    node.stuNode = nd.stuNode;
                    node.crsNode = nd.crsNode;

                    node.left_child = (delete(node.getData(), node.left_child));
                } else {
                    Node nd = get_min(node.right_child);
                    node.setData((String) nd.data);
                    node.stuNode = nd.stuNode;
                    node.crsNode = nd.crsNode;

                    node.right_child = (delete(node.getData(), node.right_child));
                }
            }
            update_height(node);
            return rotate(node);
        }



        void edit(String name ,String new_name) {
            Node node = find(name);
            insert(new_name, node.stuNode, node.crsNode);
            delete(name);
        }



        private Node<String> rotate(Node<String> node) {
            int balance = balance(node);
            if (balance > 1) {
                if (balance(node.left_child) < 0) {
                    node.left_child = (rotate_left(node.left_child));
                }
                return rotate_right(node);
            }
            if (balance < -1) {
                if (balance(node.right_child) > 0) {
                    node.right_child = (rotate_right(node.right_child));
                }
                return rotate_left(node);
            }
            return node;
        }

        private Node<String> rotate_right(Node<String> node) {
            Node<String> left_node = node.left_child;
            Node<String> center_node = left_node.right_child;
            left_node.right_child = (node);
            node.left_child = (center_node);
            update_height(node);
            update_height(left_node);
            return left_node;
        }

        private Node<String> rotate_left(Node<String> node) {
            Node<String> right_node = node.right_child;
            Node<String> center_node = right_node.left_child;
            right_node.left_child = (node);
            node.right_child = (center_node);
            update_height(node);
            update_height(right_node);
            return right_node;
        }

        private void update_height(Node<String> node) {
            int max_height = Math.max(height(node.left_child), height(node.right_child));
            node.height = (max_height + 1);
        }

        private int balance(Node<String> node) {
            if(node!=null) return height(node.left_child) - height(node.right_child);
            else return 0;
        }

        private int height(Node<String> node) {
            if(node!=null) return node.height;
            else return 0;
        }



        private Node<String> get_max(Node<String> node) {
            if (node.right_child != null) {
                return get_max(node.right_child);
            }
            return node;
        }

        private Node<String> get_min(Node<String> node) {
            if (node.left_child != null) {
                return get_min(node.left_child);
            }
            return node;
        }


        Node<String> find(String x) {
            Node<String> node = root;
            while (! x.equals(node.data)) {
                if(x.compareTo(node.data) < 0) node = node.left_child;
                else node = node.right_child;
            }
            return node;
        }

    }



    public static class HashTable {

        private class ListNode {
            Object key;
            Object value;
            ListNode next;
        }

        private ListNode[] table;

        private int count;

        int a,b,p;

        public HashTable(int a, int b, int p) {
            table = new ListNode[1];
            this.a = a;
            this.b = b;
            this.p = p;
        }

        public Object find(int code, boolean b1) {
            int a = hash(code);
            ListNode node = table[a];
            if(b1) {
                while ( ((Student)node.value).code != code ) {
                    node = node.next;
                }
            } else {
                while ( ((Course)node.value).code != code ) {
                    node = node.next;
                }
            }
            return node;
        }



        public void put(Object key, Object value) {
            int bucket = hash(key);
            ListNode list = table[bucket];
            while (list != null) {
                if (list.key.equals(key))
                    break;
                list = list.next;
            }
            if (list != null) {
                list.value = value;
            }
            else {
                if (count >= table.length) {
                    resize(true);
                }
                bucket = hash(key);
                ListNode newNode = new ListNode();
                newNode.key = key;
                newNode.value = value;
                newNode.next = table[bucket];
                table[bucket] = newNode;
                count++;
            }
        }

        public Object get(Object key) {
            int bucket = hash(key);
            ListNode list = table[bucket];
            while (list != null) {
                if (list.key.equals(key))
                    return list.value;
                list = list.next;
            }
            return null;
        }

        void print_hash() {
            for (int i = 0; i < table.length; i++) {
                ListNode node = table[i];
                while (node!=null) {
                    node = node.next;
                }
            }
        }

        public void remove(Object key) {
            if (count <= table.length/4) {
                resize(false);
            }

            int bucket = hash(key);
            if (table[bucket] == null) {
                return;
            }
            if (table[bucket].key.equals(key)) {
                table[bucket] = table[bucket].next;
                count--;
                return;
            }
            ListNode prev = table[bucket];
            ListNode curr = prev.next;
            while (curr != null && ! curr.key.equals(key)) {
                curr = curr.next;
                prev = curr;
            }
            if (curr != null) {
                prev.next = curr.next;
            }
            count--;
        }



        public int size() {
            return count;
        }

        private int hash(Object key) {
            int k = (Integer) key;
            return (int) ((((long)a*(long)k + (long)b)%p)%table.length);
        }

        private void resize(boolean c) {
            ListNode[] newtable;
            ListNode[] prevTable = table;

            if(c) newtable = new ListNode[table.length*2];
            else newtable = new ListNode[table.length/2];
            table = newtable;
            count = 0;

            for (int i = 0; i < prevTable.length; i++) {
                ListNode list = prevTable[i];
                while (list != null) {
                    put(list.key, list.value);
                    list = list.next;
                }
            }
        }

    }




    class Vertex {
        Main.List<Vertex> adj_list;
        int data;
        int index;
        Node<Course> course_pointer;
        Node<Student> student_pointer;

        public Vertex(int data) {
            this.data = data;
            adj_list = new Main.List<>();
        }

        public Vertex(int data, Node<Student> studentNode, Node<Course> courseNode) {
            this.data = data;
            student_pointer = studentNode;
            course_pointer = courseNode;
            adj_list = new Main.List<>();
        }

    }

    public class Graph {
        Main.List<Vertex> vertexs = new Main.List<>();
        int size = 0;

        void add_vertex(int code, Node<Student> studentNode, Node<Course> courseNode) {
            Vertex vertex = new Vertex(code);
            vertex.student_pointer = studentNode;
            vertex.course_pointer = courseNode;
            vertexs.addLast(vertex);
        }

        void add_vertex(Vertex vertex) {
            size++;
            vertexs.addLast(vertex);
        }


        void add_edge(Vertex vertex1, Vertex vertex2) {
            vertex1.adj_list.addLast(vertex2);
            vertex2.adj_list.addLast(vertex1);
        }

        void add_dir_edge(Vertex vertex1, Vertex vertex2) {
            vertex1.adj_list.addLast(vertex2);
        }



        boolean has_path(int c1, int c2) {
            visit = new boolean[size];
            Vertex v1 = vertexs.find(c1);
        //    System.out.println(dfs(v1, c2)+" "+c1+" "+c2);
            return dfs(v1, c2);
        }


        boolean[] visit;
        boolean dfs(Vertex start, int end) {
            if (start.data == end){
                return true;
            }
            visit[start.index] = true;
            Main.List<Vertex> list = start.adj_list;
            LNode node = list.head.next;

            while (node!=null) {
                Vertex u = (Vertex)node.value;
                if( !visit[u.index] ) {
                    if(dfs(u, end)) {
                        return true;
                    }
                }
                node = node.next;
            }
            return false;
        }


        void draw_edge(Vertex v1, Vertex v2) {
            add_edge(v1, v2);
        }

        void draw_dir_edge(Vertex v1, Vertex v2) {
            add_dir_edge(v1, v2);
        }


    }


    public class Semester {
        int sem;
        int[] courses;

        public void print() {
            System.out.print(sem+" ");
            for (int i = 0; i < courses.length; i++) {
                System.out.print(courses[i]+" ");
            }
        }
    }


    class Permutation {
        List<List<Integer>> permute(int[][] array, int size) {
            List<List<Integer>> res_list = new List<>();
            for (int i = 0; i < size; i++) {
                if(array[1][i] != 0){
                    int[][] new_array = new_array(array, size);
                    new_array[1][i]--;
                    List<List<Integer>> list = permute(new_array, size);
                    int str = array[0][i];
                    LNode<List<Integer>> node = list.head.next;

                    while (node!=null) {
                        List<Integer> l = node.value;
                        l.addLast(str);
                        res_list.addLast(l);
                        node = node.next;
                    }
                }
            }

            if(res_list.size == 0) res_list.addLast(new List<>());
            return res_list;
        }

        int[][] new_array(int[][] array, int size) {
            int[][] new_array = new int[2][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < 2; j++) {
                    new_array[j][i] = array[j][i];
                }
            }
            return new_array;
        }
    }





    public static void main(String[] args) {
        Main main = new Main();
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt();
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int p = scanner.nextInt();
        main.std_codes = new HashTable(a, b, p);
        main.crs_codes = new HashTable(a, b, p);
        main.a = a;
        main.b = b;
        main.p = p;

        boolean phase_three = false;
        boolean compare = false;
        boolean risk = false;

        for(int i=0; i<t; i++) {
            String command = scanner.next();
            switch (command) {
                case "ADDS": {
                    main.ADDS(scanner.nextInt(), scanner.next());
                    break;
                }
                case "ADDC": {
                    main.ADDC(scanner.nextInt(), scanner.next());
                    break;
                }
                case "ADDG": {
                    main.ADDG(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextDouble());
                    break;
                }
                case "EDITS": {
                    main.EDITS(scanner.nextInt(), scanner.next());
                    break;
                }
                case "EDITC": {
                    main.EDITC(scanner.nextInt(), scanner.next());
                    break;
                }
                case "EDITG": {
                    main.EDITG(scanner.nextInt(), scanner.nextInt(), scanner.nextDouble());
                    break;
                }
                case "DELETES": {
                    main.DELETES(scanner.nextInt());
                    break;
                }
                case "DELETEC": {
                    main.DELETEC(scanner.nextInt());
                    break;
                }
                case "DELETEG": {
                    main.DELETEG(scanner.nextInt(), scanner.nextInt());
                    break;
                }
                case "NUMBERS": {
                    System.out.println(main.NUMBERS(scanner.nextInt()));
                    break;
                }
                case "NUMBERC": {
                    System.out.println(main.NUMBERC(scanner.nextInt()));
                    break;
                }
                case "SEARCHSN": {
                    main.SEARCHSN(scanner.next());
                    break;
                }
                case "SEARCHCN": {
                    main.SEARCHCN(scanner.next());
                    break;
                }
                case "SEARCHSC": {
                    main.SEARCHSC(scanner.nextInt());
                    break;
                }
                case "SEARCHCC": {
                    main.SEARCHCC(scanner.nextInt());
                    break;
                }
                case "ISRELATIVE": {
                    if(!phase_three) {
                        main.draw_graph();
                        phase_three = true;
                    }
                    main.ISRELATIVE(scanner.nextInt(), scanner.nextInt());
                    break;
                }
                case "ALLRELATIVE": {
                    if(!phase_three) {
                        main.draw_graph();
                        //    main.draw_dir_graph();
                        phase_three = true;
                    }
                    main.ALLRELATIVE(scanner.nextInt());
                    break;
                }
                case "COMPARE": {
                    if(!compare) {
                        //    main.draw_graph();
                        main.draw_dir_graph();
                        compare = true;
                    }
                    main.COMPARE(scanner.nextInt(), scanner.nextInt());
                    break;
                }
                case "MINRISK": {
                    if(!risk) {
                        main.complete_desire_matrix();
                        risk = true;
                    }
                    main.MINRISK(scanner.nextInt());
                /*    try {
                        main.MINRISK(scanner.nextInt());
                    } catch (Exception e) {

                    } */
                    break;
                }
                default:
                    break;
            }
        }
        scanner.close();
    }

}


