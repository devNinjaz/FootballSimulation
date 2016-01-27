package model;

import java.util.Vector;

/**
 * Created by devninja on 8.1.16..
 */
public class Position
{
    private String pos;
    private String description;
    private String zone;
    private String[] allowed_positions_array;
    private Vector<String> allowed_positions;

    static {
    }

    /** Allowed positions
     * GK - goalkeeper
     * LB/RB - left back, right back
     * CB - centre back
     * DM - defensive midfielder
     * CM - central midfielder
     * AM - attacking midfielder
     * LW/RW - left wing, right wing
     * ST - striker
     */

    public Position(String pos)
    {
        intializePositions();
        if (allowed_positions.contains(pos)) {
            this.pos = pos;
        } else {
            System.out.println("Homie give me a normal football pos");
            System.out.println(" Allowed positions\n" +
                    "* GK - goalkeeper\n" +
                    "* LB/RB - left back, right back\n" +
                    "* CB - centre back\n" +
                    "* DM - defensive midfielder\n" +
                    "* CM - central midfielder\n" +
                    "* AM - attacking midfielder\n" +
                    "* LW/RW - left wing, right wing\n" +
                    "* ST - striker\n" +
                    "*/");
            System.exit(-1);
        }
    }

    private void intializePositions()
    {
        allowed_positions.add("gk");
        allowed_positions.add("rb");
        allowed_positions.add("lb");
        allowed_positions.add("cb");
        allowed_positions.add("cm");
        allowed_positions.add("dm");
        allowed_positions.add("am");
        allowed_positions.add("lw");
        allowed_positions.add("rw");
        allowed_positions.add("st");
    }

    private void initializeDescription()
    {
        // todo
        this.description = "AWESOME DUDE";
    }
}
