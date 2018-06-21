import java.util.Arrays;

public class Member {
    private String name;
    private long SID;
    private String password;
    private String email;
    private String number;
    private String[] teaching;
    private String learning;
    int rating;
    private long time;
    boolean needTutor;

    public Member(String name, long SID, String password, String email, String number, String[] teaching, String learning) {
        this.name = name;
        this.SID = SID;
        this.password = password;
        this.email = email;
        this.number = number;
        this.teaching = teaching;
        this.learning = learning;
        this.needTutor = true;
        this.rating = 0;
        this.time = System.currentTimeMillis(); //make sure this works
    }

    public Member(String name, long SID, String password, String email, String number, String teaching, String learning) {
        this.name = name;
        this.SID = SID;
        this.password = password;
        this.email = email;
        this.number = number;
        this.teaching = teaching.split(",");
        this.learning = learning;
        this.needTutor = true;
        this.rating = 0;
    }

    public void givePositiveRating() {
        this.rating += 1;
    }

    public void giveNegativeRating() {
        this.rating -= 1;
    }

    public int ratingIndex() {
        if (this.rating > 0) {
            return 0;
        } else if (this.rating == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    public long waitTime() {
        return System.currentTimeMillis() - this.time;
    }

    public boolean match(Member other) {
        return Arrays.asList(this.teaching).contains(other.learning) && Arrays.asList(other.teaching).contains(this.learning);
    }

    public void changeTeaching(String[] newTeaching) {
        this.teaching = newTeaching;
    }

    public void changeLearning(String newLearning) {
        this.learning = newLearning;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        return this.SID == ((Member) o).SID;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.SID);
    }

    @Override
    public String toString() {
        return "Name: " + this.name +
                "\n Email: " + this.email +
                "\n Number: " + this.number +
                "\n Teaching: " + String.join(", ", this.teaching) +
                "\n Learning: " + this.learning;
    }

    public String getName() {
        return name;
    }

    public long getSID() {
        return SID;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String[] getTeaching() {
        return teaching;
    }

    public String getLearning() {
        return learning;
    }
}
