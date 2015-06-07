package phoswald.rspt;

public enum Instruction {
    TO,     // <to:xxx> overrides the result of the following NTS.
    SET,    // <set> interprets the following TS as a set of characters
    RANGE,  // <range> interprets the following TS as a range of characters
    NOTSET, // <notset> interprets the following TS as a set of excluded characters
}
