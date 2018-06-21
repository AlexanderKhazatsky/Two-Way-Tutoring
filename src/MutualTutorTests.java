import org.junit.Test;
import java.util.Random;
import java.util.UUID;

public class MutualTutorTests {
    String[] courses = {"EE16A", "EE16B", "MATH 1B", "MATH 1A", "CS61A", "CS61B", "CS61C", "CS 170", "CS 188", "CS70", "ENGLISH 1B", "ENGLISH 1A", "PHYSICS 7A"};

    @Test
    public void makeMembers() {
        long total = 0;
        long SID = 0;
        String password = "";
        for (int attempt = 0; attempt < 1; attempt++) { //change 1 to 10 to calc average runtime
            Application app = new Application();
            Random RAND = new Random();

            for (int count = 0; count < 50000; count += 1) {
                String name = String.valueOf(count);
                SID = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
                password = String.valueOf(count);
                String email = String.valueOf(count) + "@gmail.com";
                String number = String.valueOf(count);

                String[] teaching = new String[RAND.nextInt(3) + 1];
                int index = 0;
                for (int t = 0; t < teaching.length; t++) {
                    teaching[index++] = courses[RAND.nextInt(courses.length)];
                }
                String learning = courses[RAND.nextInt(courses.length)];
                app.signUp(name, SID, password, email, number, teaching, learning);
                app.allMembers.get(SID).rating = RAND.nextInt(3) -1;
            }
            //long startTime = System.currentTimeMillis();
            for (Member member : app.allMembers.values()) {
                app.findPair(member);
                //break;
            }
            //long endTime = System.currentTimeMillis();
            String[] teaching = {"CS61A","CS61B","MATH 1B"};
            app.signUp("Sasha", 123, "temp1", "sasha@gmail.com", "646", teaching, "CS 170");

            //total += (endTime - startTime);
            app.changeLearning(123, courses[4]);
            app.rateMember(123, 1);
            //app.changeLearning(1, courses[2]);
        }
        //new Application(SID, password);
        System.out.println("Time: " + total);
    }
}
