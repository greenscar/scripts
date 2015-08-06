#!/opt/perl/bin/perl

use DBI;

$db1 = DBI->connect("DBI:mysql:oohaheatsorig;host=10.0.0.8","aubri", "z00l00");

$db2 = DBI->connect("DBI:mysql:oohaheats;host=10.0.0.8",, "aubri", "z00l00");

# 1) migrate category_def
# load all categories
$cath = $db1->prepare("select cat_descr from category_def");
$cath->execute();
while(@row = $cath->fetchrow())
{
   my($catname) = @row[0];
   $catid = "select id from categories where name like '" . $catname . "'";
   $h = $db2->prepare($catid);
   $h->execute();
   @idr = $h->fetchrow();
   if(!defined(@idr[0]))
   {
      my($stmt) = $db2->prepare("insert into categories (name, created_at, updated_at) values (?, ?, ?);");
      $stmt->bind_param(1, $catname);
      $stmt->bind_param(2, '2010-02-06 03:26:36');
      $stmt->bind_param(3, '2010-02-06 03:26:36');
      $stmt->execute();
      $stmt->finish();
   }
}
$db2->prepare("truncate  categorizations;")->execute();
$db2->prepare("truncate  recipes;")->execute();
$db2->prepare("truncate  steps;")->execute();
$db2->prepare("truncate  groceries;")->execute();
$db2->prepare("truncate  measurements;")->execute();
$db2->prepare("truncate  ingredients;")->execute();
println("truncate 1 done");
$db2->prepare("truncate  categorizations;")->execute();
$db2->prepare("truncate  recipes;")->execute();
$db2->prepare("truncate  steps;")->execute();
$db2->prepare("truncate  groceries;")->execute();
$db2->prepare("truncate  measurements;")->execute();
$db2->prepare("truncate  ingredients;")->execute();
println("truncate 2 done");
# migrate item_def
$itemh = $db1->prepare("select item_descr from item_def");
$itemh->execute();
while(@itemrow = $itemh->fetchrow())
{
   my($itemname) = @itemrow[0];
   my($insstmt) = $db2->prepare("insert into groceries (name, created_at, updated_at) values (?, ?, ?);");
   $insstmt->bind_param(1, $itemname);
   $insstmt->bind_param(2, '2010-02-06 03:26:36');
   $insstmt->bind_param(3, '2010-02-06 03:26:36');
   $insstmt->execute();
   $insstmt->finish();
}
$itemh->finish();
# load all recipes
#$rech = $db1->prepare("select r.recipe_id, r.name, c.cat_descr from recipes r, category_def c where r.cat_id = c.cat_id");
$rech = $db1->prepare("select r.recipe_id, r.name, c.cat_descr from recipes r, category_def c where r.cat_id = c.cat_id and r.name like ?");
$rech->bind_param(1, "Guacamole");
$rech->execute();
while(@recrow = $rech->fetchrow())
{
   my($orig_recipe_id) = @recrow[0];
   my($recipe_name) = @recrow[1];
   my($recipe_cat_descr) = @recrow[2];
   my($new_recipe_id) = undef;
   my($recipe_cat_new_id) = undef;
   # CHECK TO SEE IF RECIPE IS IN DB & IF NOT, INSERT IT.
   my($recsel) = $db2->prepare("select id from recipes where name like ?");
   $recsel->bind_param(1, $recipe_name);
   $recsel->execute();
   @recrow = $recsel->fetchrow();
   if(!defined(@recrow[0]))
   {
      # FETCH THE CATEGRY ID OF THIS CATEGORY FROM THE NEW TABLE.
      my($catstmt) = $db2->prepare("select id from categories where name like ?;");
      $catstmt->bind_param(1, $recipe_cat_descr);
      $catstmt->execute();
      @idr = $catstmt->fetchrow();
      $catstmt->finish();
      $recipe_cat_new_id =  @idr[0];
      #println($recipe_cat_descr . " => " . $recipe_cat_new_id);
      
      # LOAD THE NEW RECIPE_ID
      #println("select id from recipes where name like $recipe_name");
      $recstmt = $db2->prepare("select id from recipes where name like ?;");
      $recstmt->bind_param(1, $recipe_name);
      $recstmt->execute();
      @row = $recstmt->fetchrow();
      $recstmt->finish();
      $new_recipe_id = @row[0];
      if(!defined($new_recipe_id))
      {
         # INSERT THE RECIPE 
         $recstmt = $db2->prepare("insert into recipes (name, created_at, updated_at) values (?, ?, ?);");
         $recstmt->bind_param(1, $recipe_name);
         $recstmt->bind_param(2, '2010-02-06 03:26:36');
         $recstmt->bind_param(3, '2010-02-06 03:26:36');
         $recstmt->execute();
         #println("select id from recipes where name like $recipe_name");
         $recstmt = $db2->prepare("select id from recipes where name like ?;");
         $recstmt->bind_param(1, $recipe_name);
         $recstmt->execute();
         @row = $recstmt->fetchrow();
         $new_recipe_id = @row[0];
         $recstmt->finish();
      }
         
      
      # INSERT CATEGORIZATION
      $catstmt = $db2->prepare("insert into categorizations (category_id, recipe_id, created_at, updated_at) values(?, ?, ?, ?);");
      $catstmt->bind_param(1, $recipe_cat_new_id);
      $catstmt->bind_param(2, $new_recipe_id);
      $catstmt->bind_param(3, '2010-02-06 03:26:36');
      $catstmt->bind_param(4, '2010-02-06 03:26:36');
      $catstmt->execute();
      $catstmt->finish();
      # MIGRATE STEPS
      $stepstmt = $db1->prepare("select order_num, step_descr from steps where recipe_id like ? order by recipe_id, order_num;");
      $stepstmt->bind_param(1, $orig_recipe_id);
      $stepstmt->execute();
      while(@row = $stepstmt->fetchrow())
      {
         #println($row[0]);
         #println($row[1]);
         #println("insert into  steps (order, instruction, recipe_id) values (" + $row[0] + ", '" + qq($row[0]) +"', $new_recipe_id)");
         $ordernum = $row[0] + 1;
         $instruction = $row[1];
         $ins = $db2->prepare("insert into steps (recipe_id, order_num, instruction, created_at, updated_at) values (?, ?, ?, ?, ?);");
         $ins->bind_param(1, $new_recipe_id);
         $ins->bind_param(2, $ordernum);
         $ins->bind_param(3, $instruction);
         $ins->bind_param(4, '2010-02-06 03:26:36');
         $ins->bind_param(5, '2010-02-06 03:26:36');
         $ins->execute()or die "Error on insert: " . $DBI::errstr . "\n" . $new_recipe_id . "\n" . $order_num . "\n" . $instuction . "\n" . $insstmt . "\n";
         $ins->finish();
      }
      
      # MIGRATE INGREDIENTS
      my($prepstmt) = "select i.order_num, i.quantity, md.msrmnt_descr, id.item_descr"
            . " from ingredients i, item_def id, msrmnt_def md"
            . " where i.item_id = id.item_id"
            . " and i.msrmnt_id = md.msrmnt_id and i.recipe_id = ?"
            . " order by i.order_num";
      $ingstmt = $db1->prepare($prepstmt);
      my($stmt) = "select i.order_num, i.quantity, md.msrmnt_descr, id.item_descr"
            . " from ingredients i, item_def id, msrmnt_def md"
            . " where i.item_id = id.item_id"
            . " and i.msrmnt_id = md.msrmnt_id and i.recipe_id = $orig_recipe_id "
            . " order by i.order_num";
      println($stmt);
      $ingstmt->bind_param(1, $orig_recipe_id);
      $ingstmt->execute() or die "Error on insert: " . $DBI::errstr . "\n$stmt\n" ;
      while(@row = $ingstmt->fetchrow())
      {
         my($ordernum) = @row[0];
         my($quantity) = @row[1];
         my($msrmntname) = @row[2];
         my($itemname) = @row[3];
         # LOAD GROCERY ID
         $selingstmt = $db2->prepare("select id from groceries where name like ?;");
         $selingstmt->bind_param(1, $itemname);
         $selingstmt->execute();
         my($new_grocery_id) = undef;
         if(@row = $selingstmt->fetchrow())
         {
            $new_grocery_id = @row[0];
         }
         else
         {
            println("select id from groceries where name like $itemname;");
            die();
         }
         $selingstmt->finish();
         # INSERT MEASUREMENT & LOAD ID
         # 1) check to see if measurement exists. if so, load id.
         # 2) if measurement does not exist, insert then load id.
         my($new_msrmnt_name) = $quantity . " " . $msrmntname;
         my($new_msrmnt_id) = undef;
         $msrstmt = $db2->prepare("select id from measurements where name like ?;");
         $msrstmt->bind_param(1, $new_msrmnt_name);
         $msrstmt->execute() or die "Error on insert: " . $DBI::errstr . "\n";
         if(@row = $msrstmt->fetchrow())
         {
            $new_msrmnt_id = @row[0];
         }
         else
         {
            println("insert into measurements (name, created_at, updated_at) values($new_msrmnt_name, '2010-02-06 03:26:36', '2010-02-06 03:26:36')");
            $msrstmt = $db2->prepare("insert into measurements (name, created_at, updated_at) values (?, ?, ?);");
            $msrstmt->bind_param(1, $new_msrmnt_name);
            $msrstmt->bind_param(2, '2010-02-06 03:26:36');
            $msrstmt->bind_param(3, '2010-02-06 03:26:36');
            $msrstmt->execute() or die "Error on insert: " . $DBI::errstr . "\n";
            $msrstmt = $db2->prepare("select id from measurements where name like ?;");
            $msrstmt->bind_param(1, $new_msrmnt_name);
            $msrstmt->execute() or die "Error on insert: " . $DBI::errstr . "\n";;
            @row = $msrstmt->fetchrow();
            $new_msrmnt_id = @row[0];
            $msrstmt->finish();
         }
         
         # INSERT INGREDIENT USING new_ingredient_id & new_msrmnt_id
         print("insert into ingredients (recipe_id, order_num, measurement_id, grocery_id)");
         print("values ($new_recipe_id, $ordernum, $new_msrmnt_id, $new_grocery_id);\n");
         $ingstmt = $db2->prepare("insert into ingredients (recipe_id, order_num, measurement_id, grocery_id, created_at, updated_at) values (?, ?, ?, ?, ?, ?);");
         $ingstmt->bind_param(1, $new_recipe_id);
         $ingstmt->bind_param(2, $ordernum);
         $ingstmt->bind_param(3, $new_msrmnt_id);
         $ingstmt->bind_param(4, $new_grocery_id);
         $ingstmt->bind_param(5, '2010-02-06 03:26:36');
         $ingstmt->bind_param(6, '2010-02-06 03:26:36');
         $ingstmt->execute() or die "Error on insert: " . $DBI::errstr . "\n"
               . "insert into ingredients (recipe_id, order_num, measurement_id, grocery_id, created_at, updated_at) values ("
               . $new_recipe_id . ", "
               . $ordernum . ", "
               . $new_msrmnt_id . ", "
               . $new_grocery_id . ", "
               . "'2010-02-06 03:26:36', "
               . "'2010-02-06 03:26:36');\n";
         #$ingstmt->finish();
      }
   } 
}
$rech->finish();
$recsel->finish();
$db1->disconnect();
$db2->disconnect();



sub println 
{
 	my (@msg) = @_;
	foreach (@msg) 
   {
      print("$_ \n");
	}
}

# 2) migrate recipes

# 3) migrate item_def

# 4) migrate msrmnt_def

# 5) migrate ingredients

# 6) steps

