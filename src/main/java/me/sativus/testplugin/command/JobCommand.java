package me.sativus.testplugin.command;

import java.util.List;

import org.bukkit.entity.Player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.sativus.testplugin.DAO.Job;
import me.sativus.testplugin.DAO.User;
import me.sativus.testplugin.Repository.JobRepository;
import me.sativus.testplugin.Repository.UserRepository;

public class JobCommand extends BaseCommand {
    private final UserRepository userRepository = new UserRepository();
    private final JobRepository jobRepository = new JobRepository();

    @Override
    public LiteralCommandNode<CommandSourceStack> createCommand(String commandName) {
        return Commands.literal(commandName).then(Commands.literal("join").then(Commands
                .argument("job_name", StringArgumentType.word()).suggests((ctx, builder) -> {
                    List<Job> jobs = jobRepository.getAllJob();

                    for (Job iter : jobs) {
                        builder.suggest(iter.getName());
                    }
                    return builder.buildFuture();
                }).executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    String job_name = StringArgumentType.getString(ctx, "job_name");

                    User user = userRepository.findByUUID(player.getUniqueId());
                    if (user.getJob() != null) {
                        player.sendRichMessage("<red>You are joined into job already</red>");
                        return Command.SINGLE_SUCCESS;
                    }

                    Job job = jobRepository.findByName(job_name);
                    if (job == null) {
                        player.sendRichMessage(
                                "<red>Job not found, use /job list to list available jobs</red>");
                        return Command.SINGLE_SUCCESS;
                    }

                    user.setJob(job);

                    userRepository.save(user);

                    player.sendRichMessage("<green>You are joined into job: </green><aqua>"
                            + job.getName() + "</aqua>");

                    return Command.SINGLE_SUCCESS;
                }))).then(Commands.literal("list").executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    List<Job> jobs = jobRepository.getAllJob();

                    for (Job job : jobs) {
                        player.sendRichMessage(job.getName());
                    }

                    return Command.SINGLE_SUCCESS;
                }))
                // .then(Commands.literal("leave"))
                .build();
    }

}
