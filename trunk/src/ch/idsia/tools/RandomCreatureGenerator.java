package ch.idsia.tools;

import ch.idsia.benchmark.mario.engine.sprites.Sprite;

import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: Sep 17, 2010
 * Time: 1:38:10 AM
 * Package: ch.idsia.tools
 */
public class RandomCreatureGenerator extends Random
{
private HashSet<String> allowedCreatures = new HashSet<String>();
private boolean creaturesEnabled = true;
private boolean kindByDifficulty = true;
private int difficulty = 0;
private final String[] kinds = {"g", "gw", "gk", "gkw", "rk", "rkw", "s", "sw"};

public RandomCreatureGenerator(long seed, String creatures, int difficulty)
{
    super(seed);
    this.setSeed(seed, creatures, difficulty);
}

public void setSeed(long seed, String creatures, int difficulty)
{
    this.setSeed(seed);
    this.difficulty = difficulty;

    if (creatures.equals("off"))
    {
        creaturesEnabled = false;
    } else
    {
        creaturesEnabled = true;

        if (!creatures.equals(""))
        {
            kindByDifficulty = false;
            Pattern pattern = Pattern.compile("[gsfr]k?w?(:\\d+)?");
            Matcher matcher = pattern.matcher(creatures);

            while (matcher.find())
            {
                String group = matcher.group();
                String[] subgroups = group.split(":");
                allowedCreatures.add(subgroups[0]);
            }
        } else
        {
            kindByDifficulty = true;
            allowedCreatures.add("f");
        }
    }
//    System.out.println(allowedCreatures.toString());
}

public boolean canAdd()
{
    return creaturesEnabled;
}

private int getCreatureType(String type)
{
    int kind = Sprite.KIND_UNDEF;
    try
    {
        switch (type.charAt(0))
        {
            case 'g':
                kind = Sprite.KIND_GOOMBA;
                if (type.charAt(1) == 'w')
                {
                    kind = Sprite.KIND_GOOMBA_WINGED;
                } else if (type.charAt(1) == 'k')
                {
                    kind = Sprite.KIND_GREEN_KOOPA;
                    if (type.charAt(2) == 'w')
                        kind = Sprite.KIND_GREEN_KOOPA_WINGED;
                }
                break;
            case 'r':
                kind = Sprite.KIND_RED_KOOPA;
                if (type.charAt(2) == 'w')
                    kind = Sprite.KIND_RED_KOOPA_WINGED;
                break;
            case 's':
                kind = Sprite.KIND_SPIKY;
                if (type.charAt(1) == 'w')
                    kind = Sprite.KIND_SPIKY_WINGED;
                break;
        }
    } catch (Exception e)
    {
        return kind;
    }

    return kind;
}

public int getNextCreature()
{
    int kind = Sprite.KIND_UNDEF;

    if (kindByDifficulty)
    {
        kind = getCreatureType (kinds[this.nextInt(8)]);
        if (difficulty < 1)
        {
            kind = this.getCreatureType("g");
        } else if (difficulty < 3)
        {
            String type = kinds[this.nextInt(6)];
            kind = this.getCreatureType(type);
        }
    } else
    {
        Object[] localKinds = allowedCreatures.toArray();
        kind = this.getCreatureType((String)localKinds[this.nextInt(localKinds.length)]);
    }

    return kind;
}

public boolean isCreatureEnabled(String creature)
{
    return allowedCreatures.contains(creature);
}
}
