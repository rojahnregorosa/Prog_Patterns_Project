package org.example.controller;
import org.example.model.Employee;
import org.example.model.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmployeeController extends UserController {
    private List<Member> members;
    private List<Employee> employees;

    public EmployeeController() {
        super();
        members = new ArrayList<>();
        employees = new ArrayList<>();
    }

    // Method to add a new member
    public boolean addMember(Member member) {
        if (member != null && !members.contains(member)) {
            members.add(member);
            return true;
        }
        return false;
    }

    // Method to remove a member by their memberID
    public boolean removeMember(String memberID) {
        return members.removeIf(member -> Objects.equals(member.getMemberId(), memberID));
    }

    // Method to update an existing member
    public boolean updateMember(String memberID, Member updatedMember) {
        for (int i = 0; i < members.size(); i++) {
            if (Objects.equals(members.get(i).getMemberId(), memberID)) {
                members.set(i, updatedMember);
                return true;
            }
        }
        return false;
    }

    // Method to view a member by their memberID
    public Member viewMember(String memberID) {
        for (Member member : members) {
            if (Objects.equals(member.getMemberId(), memberID)) {
                return member;
            }
        }
        return null;
    }
}
