Lehasa Seoe
SXXLEH001

The approach is using string manipulation, read in the 8byte addresses, convert them to 64-bit addresses, use the last 12-bits. Of the 12, 7 bits are the offset and the first 5 are used to check against the page table. The value being mapped to is converted to binary, concatenated with the offset  then converted to hex to get the physical address.
