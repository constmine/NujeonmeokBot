package io.github.constmine.bot.tools;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageMember {

    public static List<Member> getAllMember(Guild guild) {
        List<Member> mems = new ArrayList<>();
        guild.loadMembers().onSuccess(mems::addAll);
        return mems;
    }

    @Nullable
    public static List<Member> getRoleMember(Guild guild, String... role_names) {
        List<Role> roles = new ArrayList<>();
        for(Role role : guild.getRoles()) {
            for(String role_name : role_names) {
                role_name = role_name.replaceAll("[<@&>]", "");

                // id로 검색이 가능할때
                try {
                    if (guild.getRoleById(role_name) != null) {
                        role_name = Objects.requireNonNull(guild.getRoleById(role_name)).getName();
                    }
                } catch (NumberFormatException ignored) {}


                // "everyone" 받아왔을때는 모두 추가
                if(role_name.equalsIgnoreCase(role.getName().replaceAll("[<@&>]", ""))) {
                    roles.add(role);
                }

            }
        }


        if(roles.isEmpty()) return null;
        return guild.getMembersWithRoles(roles);
    }


}
