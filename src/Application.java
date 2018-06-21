import java.util.HashMap;
import java.util.Comparator;
import java.util.Arrays;
import java.util.PriorityQueue;




///// ISSUE!!!!!! WHEN YOU RATE A MEMBER, IF THIS CHANGES THEIR QUEUE,
///// YOU NEED TO REMOVE AND RE-ADD THE MEMBER TO THEIR NEW, CORRECT QUEUE

public class Application {
    HashMap<Long, Member> allMembers = new HashMap<>();
    HashMap<String, HashMap<String, PriorityQueue<Member>[]>> teachers = new HashMap<>();  //Make priority queue?

    Application() {
    }

    public void signUp(String name, long SID, String password, String email, String number, String[] teaching, String learning) {
        //System.out.println("Signing up... ");
        if (allMembers.containsKey(SID)) {
            //System.out.println("It appears that you already have an account. Please sign in");
            return;
        }
        Member newMember = new Member(name, SID, password, email, number, teaching, learning);
        allMembers.put(SID, newMember);
        addMember(newMember);
        //currMember = newMember;
        System.out.println("Welcome, " + name + "!");
        System.out.println("I see you tutor: " + String.join(", ", teaching));
        System.out.println("I see you want a tutor for: " + learning);
    }

    public void signIn(long SID, String password) {
        System.out.println("Signing in...");

        if (allMembers.get(SID).getPassword().equals(password)) {
            //System.out.println("Welcome, " + allMembers.get(SID).getName() + "!");
            //this.currMember = allMembers.get(SID);
        } else {
            //System.out.println("Sorry, that SID is either not registered yet, or the password you entered was incorrect.");
        }
    }

    public void signOut() {
        //System.out.println("Signing out...");
        //this.currMember = null;
    }

    public void findPair(Member member) {
        System.out.println("Hello " + member.getName() + "!");
        System.out.println("I see you tutor: " + String.join(", ", member.getTeaching()));
        System.out.println("I see you want a tutor for: " + member.getLearning());

        if (!member.needTutor) {
            System.out.println("Sorry, you were already matched with a tutor.");
            return;
        }

        Member pair = null;

        for (int queueIndex = member.ratingIndex(); queueIndex <= 2; queueIndex++) {
            for (String course : member.getTeaching()) {

                if (pair != null) break;

                try {
                    PriorityQueue<Member> potentials = teachers.get(member.getLearning()).get(course)[queueIndex];
                    for (Member other : potentials) {
                        if (!other.equals(member)) {
                            pair = other;
                            break;
                        }
                    }
                } catch (NullPointerException e) {
                    //do nothing
                }
            }
        }

        if (pair != null) {
            removeMember(pair);
            removeMember(member);
            System.out.println("Congrats " + member.getName() + "! Meet your new Mutual Tutor:");
            System.out.println("Your rating " + member.ratingIndex());
            System.out.println("Your tutor's rating " + pair.ratingIndex());
            System.out.println(pair);
            //Ask member if they would like to stay in still?
        } else {
            System.out.println("Sorry " + member.getName() + ", we don't appear to have any matches for you right now. " +
                    "We will contact you when we find one!");
        }
    }

    private void addMember(Member newMember) {
        newMember.needTutor = true;
        for (String course : newMember.getTeaching()) {
            if (!teachers.containsKey(course)) {
                teachers.put(course, new HashMap<>());
            }
            if (!teachers.get(course).containsKey(newMember.getLearning())) {
                PriorityQueue[] queueArray = new PriorityQueue[3];
                for (int i = 0; i <= 2; i++) {
                    queueArray[i] = new PriorityQueue<>(new InterestComparator(course));
                }
                teachers.get(course).put(newMember.getLearning(), queueArray);
            }
            PriorityQueue possible = teachers.get(course).get(newMember.getLearning())[newMember.ratingIndex()];
            possible.add(newMember);
            //possible.sort(new InterestComparator(course));
        }
    }

    private void removeMember(Member oldMember) {
        oldMember.needTutor = false;
        try {
            for (String course : oldMember.getTeaching()) {
                teachers.get(course).get(oldMember.getLearning())[oldMember.ratingIndex()].remove(oldMember);
            }
        } catch (NullPointerException e) {
            //do nothing
        }
    }

    void changeTeaching(long SID, String[] newTeaching) {
        Member changedMember = this.allMembers.get(SID);
        removeMember(changedMember);
        changedMember.changeTeaching(newTeaching);
        addMember(changedMember);
    }

    void changeLearning(long SID, String newLearning) {
        Member changedMember = this.allMembers.get(SID);
        removeMember(changedMember);
        changedMember.changeLearning(newLearning);
        addMember(changedMember);
    }

    void rateMember(long SID, int rating) {
        Member memberRated = allMembers.get(SID);
        removeMember(memberRated);
        if (rating == 1) {
            memberRated.givePositiveRating();
        }
        if (rating == -1) {
            memberRated.giveNegativeRating();
        }
        addMember(memberRated);
    }

    public class InterestComparator implements Comparator {
        String course;

        InterestComparator(String course) {
            this.course = course;
        }

        @Override
        public int compare(Object o1, Object o2) {
            Member member1 = (Member) o1;
            Member member2 = (Member) o2;
            if (Arrays.asList(member1.getTeaching()).indexOf(this.course) < Arrays.asList(member2.getTeaching()).indexOf(this.course)) {
                return -1;
            } else if (Arrays.asList(member1.getTeaching()).indexOf(this.course) > Arrays.asList(member2.getTeaching()).indexOf(this.course)) {
                return 1;
            } else if (member1.waitTime() > member2.waitTime()) {
                return -1;
            } else if (member1.waitTime() < member2.waitTime()) {
                return 1;
            } else {
                return 0;
            }
            //return Arrays.asList(member1.getTeaching()).indexOf(this.course) - Arrays.asList(member2.getTeaching()).indexOf(this.course);
        }
    }
}

//Go down queues until you find a match