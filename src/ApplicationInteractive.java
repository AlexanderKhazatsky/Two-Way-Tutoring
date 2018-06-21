import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

//Notes priority queue of best rated

public class ApplicationInteractive {
    private static HashMap<Long, Member> allMembers = new HashMap<>();
    private static HashMap<String, HashMap<String, LinkedList<Member>>> teachers = new HashMap<>();  //Make priority queue?
    private Member currMember;

    ApplicationInteractive() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Welcome! Would you like to sign in or sign up?");

        while (true) {
            String answer = reader.nextLine();
            if (answer.equals("sign up")) {
                signUp();
                break;
            } else if (answer.equals("sign in")) {
                signIn();
                break;
            } else {
                System.out.println("Sorry that isn't a possible answer. Would you like to sign in or sign up?");
            }
        }
        findPair();
    }

    private void signUp() {
        Scanner reader = new Scanner(System.in);
        System.out.println("Please enter your name: ");
        String name = reader.nextLine();
        System.out.println("Please enter your SID: ");
        String stringSID = reader.nextLine();
        long SID = Long.parseLong(stringSID);

        if (this.allMembers.containsKey(SID)) {
            System.out.println("It appears that you already have an account. Please sign in");
            return;
        }
        System.out.println("Please enter your desired Password: ");
        String password = reader.nextLine();
        System.out.println("Please enter your Email: ");
        String email = reader.nextLine();
        System.out.println("Please enter your Phone Number: ");
        String number = reader.nextLine();
        System.out.println("Please enter the courses you would like to tutor (separated by commas): ");
        String teaching = reader.nextLine();
        System.out.println("Please enter your the course you would like to be tutored in: ");
        String learning = reader.nextLine();

        Member newMember = new Member(name, SID, password, email, number, teaching, learning);
        allMembers.put(SID, newMember);
        addMember(newMember);
        this.currMember = newMember;
        System.out.println("Welcome, " + name + "!");
    }

    private void signIn() {
        Scanner reader = new Scanner(System.in);
        long SID;
        String password;

        while (true) {
            System.out.println("SID: ");
            String stringSID = reader.nextLine();
            SID = Long.parseLong(stringSID);
            System.out.println("Password: ");
            password = reader.nextLine();

            if (allMembers.get(SID).getPassword().equals(password)) {
                System.out.println("Welcome, " + allMembers.get(SID).getName() + "!");
                this.currMember = allMembers.get(SID);
                break;
            }
            System.out.println("Sorry, that SID is either not registered yet, or the password you entered was incorrect.");
        }
    }

    public void signOut() {
        this.currMember = null;
    }

    public void findPair() {
        if (this.currMember == null) {
            System.out.println("Please sign in before trying to find a pair!");
            return;
        }

        Member pair = null;
        for (String course : this.currMember.getTeaching()) {
            if (pair != null) break;

            try {
                LinkedList<Member> potentials = teachers.get(this.currMember.getLearning()).get(course);
                for (Member other : potentials) {
                    if (!other.equals(this.currMember)) {
                        pair = other;
                        break;
                    }
                }
            } catch (NullPointerException e) {
                //do nothing
            }
        }

        if (pair != null) {
            removeMember(pair);
            removeMember(this.currMember);
            System.out.println("Meet your new 'Mutual tutor', " + this.currMember.getName() + "!");
            System.out.println(pair);
            //Ask member if they would like to stay in still?
        } else {
            System.out.println("Sorry, we don't appear to have any matches for you right now. " +
                    "We will contact you when we find one!");
        }
    }

    private static void addMember(Member newMember) {
        for (String course : newMember.getTeaching()) {
            if (!teachers.containsKey(course)) {
                teachers.put(course, new HashMap<>());
            }
            if (!teachers.get(course).containsKey(newMember.getLearning())) {
                teachers.get(course).put(newMember.getLearning(), new LinkedList<>());
            }
            teachers.get(course).get(newMember.getLearning()).addLast(newMember);
        }
    }

    private void removeMember(Member oldMember) {
        try {
            for (String course : oldMember.getTeaching()) {
                teachers.get(course).get(oldMember.getLearning()).remove(oldMember);
            }
        } catch (Exception e) {
            //do nothing
        }
    }

    private void changeTeaching(String[] newTeaching) {
        if (this.currMember == null) {
            System.out.println("Please sign in before changing your settings!");
            return;
        }

        removeMember(this.currMember);
        this.currMember.changeTeaching(newTeaching);
        addMember(this.currMember);
    }

    private void changeLearning(String newLearning) {
        if (this.currMember == null) {
            System.out.println("Please sign in before changing your settings");
            return;
        }

        removeMember(this.currMember);
        this.currMember.changeLearning(newLearning);
        addMember(this.currMember);
    }

    public static void main(String[] args) {
        addMember(new Member("Sasha", 123, "temp1", "sasha@gmail.com", "646", "CS61A,CS61B,MATH1B", "CS170"));
        addMember(new Member("Gaby", 124, "temp2", "gaby@gmail.com", "645", "CS170,CS61B,MATH1B", "MATH1B"));

        //AUTOMIZE TESTING
        //CONSIDER TAKING AWAY CURRUSER?

        while (true) {
            new ApplicationInteractive();
        }
    }
}
