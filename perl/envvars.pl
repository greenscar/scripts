while (($key, $val) = each(%ENV))
{
    print ($key . " => " . $val . "\n");
}
