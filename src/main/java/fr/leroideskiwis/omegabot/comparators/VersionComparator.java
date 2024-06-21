package fr.leroideskiwis.omegabot.comparators;

public class VersionComparator implements java.util.Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        if(o1.equals(o2)) return 0;

        String[] split1 = o1.split("\\.");
        String[] split2 = o2.split("\\.");

        for (int i = 0; i < Math.max(split1.length, split2.length); i++) {
            if(i >= split1.length || split1[i].isEmpty()) return -1;
            if(i >= split2.length  || split2[i].isEmpty()) return 1;
            int num1 = Integer.parseInt(split1[i]);
            int num2 = Integer.parseInt(split2[i]);
            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }

        return 0;

    }
}
