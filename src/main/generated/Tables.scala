package generated
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Activity.schema, Category.schema, Company.schema, Country.schema, Outputtype.schema, Outputtypeactivitymapping.schema, Product.schema, Productcategorymapping.schema, Vehicle.schema, Vehiclecategory.schema, Vehicletype.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Activity
   *  @param id Database column ID SqlType(bigserial), AutoInc, PrimaryKey
   *  @param name Database column NAME SqlType(varchar), Length(200,true) */
  case class ActivityRow(id: Long, name: String)
  /** GetResult implicit for fetching ActivityRow objects using plain SQL queries */
  implicit def GetResultActivityRow(implicit e0: GR[Long], e1: GR[String]): GR[ActivityRow] = GR{
    prs => import prs._
    ActivityRow.tupled((<<[Long], <<[String]))
  }
  /** Table description of table Activity. Objects of this class serve as prototypes for rows in queries. */
  class Activity(_tableTag: Tag) extends profile.api.Table[ActivityRow](_tableTag, Some("practice"), "Activity") {
    def * = (id, name) <> (ActivityRow.tupled, ActivityRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> ActivityRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column NAME SqlType(varchar), Length(200,true) */
    val name: Rep[String] = column[String]("NAME", O.Length(200,varying=true))
  }
  /** Collection-like TableQuery object for table Activity */
  lazy val Activity = new TableQuery(tag => new Activity(tag))

  /** Entity class storing rows of table Category
   *  @param catId Database column CAT_ID SqlType(bigserial), AutoInc, PrimaryKey
   *  @param catName Database column CAT_NAME SqlType(varchar), Length(200,true)
   *  @param catDesc Database column CAT_DESC SqlType(varchar), Length(200,true) */
  case class CategoryRow(catId: Long, catName: String, catDesc: String)
  /** GetResult implicit for fetching CategoryRow objects using plain SQL queries */
  implicit def GetResultCategoryRow(implicit e0: GR[Long], e1: GR[String]): GR[CategoryRow] = GR{
    prs => import prs._
    CategoryRow.tupled((<<[Long], <<[String], <<[String]))
  }
  /** Table description of table Category. Objects of this class serve as prototypes for rows in queries. */
  class Category(_tableTag: Tag) extends profile.api.Table[CategoryRow](_tableTag, Some("practice"), "Category") {
    def * = (catId, catName, catDesc) <> (CategoryRow.tupled, CategoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(catId), Rep.Some(catName), Rep.Some(catDesc)).shaped.<>({r=>import r._; _1.map(_=> CategoryRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column CAT_ID SqlType(bigserial), AutoInc, PrimaryKey */
    val catId: Rep[Long] = column[Long]("CAT_ID", O.AutoInc, O.PrimaryKey)
    /** Database column CAT_NAME SqlType(varchar), Length(200,true) */
    val catName: Rep[String] = column[String]("CAT_NAME", O.Length(200,varying=true))
    /** Database column CAT_DESC SqlType(varchar), Length(200,true) */
    val catDesc: Rep[String] = column[String]("CAT_DESC", O.Length(200,varying=true))
  }
  /** Collection-like TableQuery object for table Category */
  lazy val Category = new TableQuery(tag => new Category(tag))

  /** Entity class storing rows of table Company
   *  @param companyid Database column companyId SqlType(bigserial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(200,true)
   *  @param description Database column description SqlType(varchar), Length(200,true), Default(None)
   *  @param licencenumber Database column licenceNumber SqlType(varchar), Length(200,true)
   *  @param country Database column country SqlType(int8)
   *  @param startyear Database column startYear SqlType(timestamp) */
  case class CompanyRow(companyid: Long, name: String, description: Option[String] = None, licencenumber: String, country: Long, startyear: java.sql.Timestamp)
  /** GetResult implicit for fetching CompanyRow objects using plain SQL queries */
  implicit def GetResultCompanyRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[java.sql.Timestamp]): GR[CompanyRow] = GR{
    prs => import prs._
    CompanyRow.tupled((<<[Long], <<[String], <<?[String], <<[String], <<[Long], <<[java.sql.Timestamp]))
  }
  /** Table description of table Company. Objects of this class serve as prototypes for rows in queries. */
  class Company(_tableTag: Tag) extends profile.api.Table[CompanyRow](_tableTag, Some("vehicle"), "Company") {
    def * = (companyid, name, description, licencenumber, country, startyear) <> (CompanyRow.tupled, CompanyRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(companyid), Rep.Some(name), description, Rep.Some(licencenumber), Rep.Some(country), Rep.Some(startyear)).shaped.<>({r=>import r._; _1.map(_=> CompanyRow.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column companyId SqlType(bigserial), AutoInc, PrimaryKey */
    val companyid: Rep[Long] = column[Long]("companyId", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(200,true) */
    val name: Rep[String] = column[String]("name", O.Length(200,varying=true))
    /** Database column description SqlType(varchar), Length(200,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(200,varying=true), O.Default(None))
    /** Database column licenceNumber SqlType(varchar), Length(200,true) */
    val licencenumber: Rep[String] = column[String]("licenceNumber", O.Length(200,varying=true))
    /** Database column country SqlType(int8) */
    val country: Rep[Long] = column[Long]("country")
    /** Database column startYear SqlType(timestamp) */
    val startyear: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("startYear")

    /** Foreign key referencing Country (database name Company_country_fkey) */
    lazy val countryFk = foreignKey("Company_country_fkey", country, Country)(r => r.countryid, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Company */
  lazy val Company = new TableQuery(tag => new Company(tag))

  /** Entity class storing rows of table Country
   *  @param countryid Database column countryId SqlType(bigserial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(200,true)
   *  @param language Database column language SqlType(varchar), Length(200,true)
   *  @param code Database column code SqlType(varchar), Length(200,true) */
  case class CountryRow(countryid: Long, name: String, language: String, code: String)
  /** GetResult implicit for fetching CountryRow objects using plain SQL queries */
  implicit def GetResultCountryRow(implicit e0: GR[Long], e1: GR[String]): GR[CountryRow] = GR{
    prs => import prs._
    CountryRow.tupled((<<[Long], <<[String], <<[String], <<[String]))
  }
  /** Table description of table Country. Objects of this class serve as prototypes for rows in queries. */
  class Country(_tableTag: Tag) extends profile.api.Table[CountryRow](_tableTag, Some("vehicle"), "Country") {
    def * = (countryid, name, language, code) <> (CountryRow.tupled, CountryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(countryid), Rep.Some(name), Rep.Some(language), Rep.Some(code)).shaped.<>({r=>import r._; _1.map(_=> CountryRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column countryId SqlType(bigserial), AutoInc, PrimaryKey */
    val countryid: Rep[Long] = column[Long]("countryId", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(200,true) */
    val name: Rep[String] = column[String]("name", O.Length(200,varying=true))
    /** Database column language SqlType(varchar), Length(200,true) */
    val language: Rep[String] = column[String]("language", O.Length(200,varying=true))
    /** Database column code SqlType(varchar), Length(200,true) */
    val code: Rep[String] = column[String]("code", O.Length(200,varying=true))
  }
  /** Collection-like TableQuery object for table Country */
  lazy val Country = new TableQuery(tag => new Country(tag))

  /** Entity class storing rows of table Outputtype
   *  @param id Database column ID SqlType(bigserial), AutoInc, PrimaryKey
   *  @param name Database column NAME SqlType(varchar), Length(200,true) */
  case class OutputtypeRow(id: Long, name: String)
  /** GetResult implicit for fetching OutputtypeRow objects using plain SQL queries */
  implicit def GetResultOutputtypeRow(implicit e0: GR[Long], e1: GR[String]): GR[OutputtypeRow] = GR{
    prs => import prs._
    OutputtypeRow.tupled((<<[Long], <<[String]))
  }
  /** Table description of table OutputType. Objects of this class serve as prototypes for rows in queries. */
  class Outputtype(_tableTag: Tag) extends profile.api.Table[OutputtypeRow](_tableTag, Some("practice"), "OutputType") {
    def * = (id, name) <> (OutputtypeRow.tupled, OutputtypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name)).shaped.<>({r=>import r._; _1.map(_=> OutputtypeRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column NAME SqlType(varchar), Length(200,true) */
    val name: Rep[String] = column[String]("NAME", O.Length(200,varying=true))
  }
  /** Collection-like TableQuery object for table Outputtype */
  lazy val Outputtype = new TableQuery(tag => new Outputtype(tag))

  /** Entity class storing rows of table Outputtypeactivitymapping
   *  @param id Database column ID SqlType(bigserial), AutoInc, PrimaryKey
   *  @param outputId Database column OUTPUT_ID SqlType(int8)
   *  @param activityId Database column ACTIVITY_ID SqlType(int8) */
  case class OutputtypeactivitymappingRow(id: Long, outputId: Long, activityId: Long)
  /** GetResult implicit for fetching OutputtypeactivitymappingRow objects using plain SQL queries */
  implicit def GetResultOutputtypeactivitymappingRow(implicit e0: GR[Long]): GR[OutputtypeactivitymappingRow] = GR{
    prs => import prs._
    OutputtypeactivitymappingRow.tupled((<<[Long], <<[Long], <<[Long]))
  }
  /** Table description of table OutputTypeActivityMapping. Objects of this class serve as prototypes for rows in queries. */
  class Outputtypeactivitymapping(_tableTag: Tag) extends profile.api.Table[OutputtypeactivitymappingRow](_tableTag, Some("practice"), "OutputTypeActivityMapping") {
    def * = (id, outputId, activityId) <> (OutputtypeactivitymappingRow.tupled, OutputtypeactivitymappingRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(outputId), Rep.Some(activityId)).shaped.<>({r=>import r._; _1.map(_=> OutputtypeactivitymappingRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column OUTPUT_ID SqlType(int8) */
    val outputId: Rep[Long] = column[Long]("OUTPUT_ID")
    /** Database column ACTIVITY_ID SqlType(int8) */
    val activityId: Rep[Long] = column[Long]("ACTIVITY_ID")
  }
  /** Collection-like TableQuery object for table Outputtypeactivitymapping */
  lazy val Outputtypeactivitymapping = new TableQuery(tag => new Outputtypeactivitymapping(tag))

  /** Entity class storing rows of table Product
   *  @param prodId Database column PROD_ID SqlType(bigserial), AutoInc, PrimaryKey
   *  @param prodName Database column PROD_NAME SqlType(varchar), Length(200,true)
   *  @param prodDesc Database column PROD_DESC SqlType(varchar), Length(200,true) */
  case class ProductRow(prodId: Long, prodName: String, prodDesc: String)
  /** GetResult implicit for fetching ProductRow objects using plain SQL queries */
  implicit def GetResultProductRow(implicit e0: GR[Long], e1: GR[String]): GR[ProductRow] = GR{
    prs => import prs._
    ProductRow.tupled((<<[Long], <<[String], <<[String]))
  }
  /** Table description of table Product. Objects of this class serve as prototypes for rows in queries. */
  class Product(_tableTag: Tag) extends profile.api.Table[ProductRow](_tableTag, Some("practice"), "Product") {
    def * = (prodId, prodName, prodDesc) <> (ProductRow.tupled, ProductRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(prodId), Rep.Some(prodName), Rep.Some(prodDesc)).shaped.<>({r=>import r._; _1.map(_=> ProductRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column PROD_ID SqlType(bigserial), AutoInc, PrimaryKey */
    val prodId: Rep[Long] = column[Long]("PROD_ID", O.AutoInc, O.PrimaryKey)
    /** Database column PROD_NAME SqlType(varchar), Length(200,true) */
    val prodName: Rep[String] = column[String]("PROD_NAME", O.Length(200,varying=true))
    /** Database column PROD_DESC SqlType(varchar), Length(200,true) */
    val prodDesc: Rep[String] = column[String]("PROD_DESC", O.Length(200,varying=true))
  }
  /** Collection-like TableQuery object for table Product */
  lazy val Product = new TableQuery(tag => new Product(tag))

  /** Entity class storing rows of table Productcategorymapping
   *  @param mapId Database column MAP_ID SqlType(bigserial), AutoInc, PrimaryKey
   *  @param prodId Database column PROD_ID SqlType(int8)
   *  @param catId Database column CAT_ID SqlType(int8) */
  case class ProductcategorymappingRow(mapId: Long, prodId: Long, catId: Long)
  /** GetResult implicit for fetching ProductcategorymappingRow objects using plain SQL queries */
  implicit def GetResultProductcategorymappingRow(implicit e0: GR[Long]): GR[ProductcategorymappingRow] = GR{
    prs => import prs._
    ProductcategorymappingRow.tupled((<<[Long], <<[Long], <<[Long]))
  }
  /** Table description of table ProductCategoryMapping. Objects of this class serve as prototypes for rows in queries. */
  class Productcategorymapping(_tableTag: Tag) extends profile.api.Table[ProductcategorymappingRow](_tableTag, Some("practice"), "ProductCategoryMapping") {
    def * = (mapId, prodId, catId) <> (ProductcategorymappingRow.tupled, ProductcategorymappingRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(mapId), Rep.Some(prodId), Rep.Some(catId)).shaped.<>({r=>import r._; _1.map(_=> ProductcategorymappingRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MAP_ID SqlType(bigserial), AutoInc, PrimaryKey */
    val mapId: Rep[Long] = column[Long]("MAP_ID", O.AutoInc, O.PrimaryKey)
    /** Database column PROD_ID SqlType(int8) */
    val prodId: Rep[Long] = column[Long]("PROD_ID")
    /** Database column CAT_ID SqlType(int8) */
    val catId: Rep[Long] = column[Long]("CAT_ID")
  }
  /** Collection-like TableQuery object for table Productcategorymapping */
  lazy val Productcategorymapping = new TableQuery(tag => new Productcategorymapping(tag))

  /** Entity class storing rows of table Vehicle
   *  @param vehicleid Database column vehicleId SqlType(bigserial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(200,true)
   *  @param description Database column description SqlType(varchar), Length(200,true), Default(None)
   *  @param modelnumber Database column modelNumber SqlType(varchar), Length(200,true)
   *  @param vehicletype Database column vehicleType SqlType(int8)
   *  @param company Database column company SqlType(int8)
   *  @param quantity Database column quantity SqlType(int8)
   *  @param weight Database column weight SqlType(int8) */
  case class VehicleRow(vehicleid: Long, name: String, description: Option[String] = None, modelnumber: String, vehicletype: Long, company: Long, quantity: Long, weight: Long)
  /** GetResult implicit for fetching VehicleRow objects using plain SQL queries */
  implicit def GetResultVehicleRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]]): GR[VehicleRow] = GR{
    prs => import prs._
    VehicleRow.tupled((<<[Long], <<[String], <<?[String], <<[String], <<[Long], <<[Long], <<[Long], <<[Long]))
  }
  /** Table description of table Vehicle. Objects of this class serve as prototypes for rows in queries. */
  class Vehicle(_tableTag: Tag) extends profile.api.Table[VehicleRow](_tableTag, Some("vehicle"), "Vehicle") {
    def * = (vehicleid, name, description, modelnumber, vehicletype, company, quantity, weight) <> (VehicleRow.tupled, VehicleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(vehicleid), Rep.Some(name), description, Rep.Some(modelnumber), Rep.Some(vehicletype), Rep.Some(company), Rep.Some(quantity), Rep.Some(weight)).shaped.<>({r=>import r._; _1.map(_=> VehicleRow.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column vehicleId SqlType(bigserial), AutoInc, PrimaryKey */
    val vehicleid: Rep[Long] = column[Long]("vehicleId", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(200,true) */
    val name: Rep[String] = column[String]("name", O.Length(200,varying=true))
    /** Database column description SqlType(varchar), Length(200,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(200,varying=true), O.Default(None))
    /** Database column modelNumber SqlType(varchar), Length(200,true) */
    val modelnumber: Rep[String] = column[String]("modelNumber", O.Length(200,varying=true))
    /** Database column vehicleType SqlType(int8) */
    val vehicletype: Rep[Long] = column[Long]("vehicleType")
    /** Database column company SqlType(int8) */
    val company: Rep[Long] = column[Long]("company")
    /** Database column quantity SqlType(int8) */
    val quantity: Rep[Long] = column[Long]("quantity")
    /** Database column weight SqlType(int8) */
    val weight: Rep[Long] = column[Long]("weight")

    /** Foreign key referencing Company (database name Vehicle_company_fkey) */
    lazy val companyFk = foreignKey("Vehicle_company_fkey", company, Company)(r => r.companyid, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Vehicletype (database name Vehicle_vehicleType_fkey) */
    lazy val vehicletypeFk = foreignKey("Vehicle_vehicleType_fkey", vehicletype, Vehicletype)(r => r.vehicletypeid, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Vehicle */
  lazy val Vehicle = new TableQuery(tag => new Vehicle(tag))

  /** Entity class storing rows of table Vehiclecategory
   *  @param vehiclecategoryid Database column vehicleCategoryId SqlType(bigserial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(200,true)
   *  @param description Database column description SqlType(varchar), Length(200,true), Default(None)
   *  @param maxcapacity Database column maxCapacity SqlType(float8) */
  case class VehiclecategoryRow(vehiclecategoryid: Long, name: String, description: Option[String] = None, maxcapacity: Double)
  /** GetResult implicit for fetching VehiclecategoryRow objects using plain SQL queries */
  implicit def GetResultVehiclecategoryRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[Double]): GR[VehiclecategoryRow] = GR{
    prs => import prs._
    VehiclecategoryRow.tupled((<<[Long], <<[String], <<?[String], <<[Double]))
  }
  /** Table description of table VehicleCategory. Objects of this class serve as prototypes for rows in queries. */
  class Vehiclecategory(_tableTag: Tag) extends profile.api.Table[VehiclecategoryRow](_tableTag, Some("vehicle"), "VehicleCategory") {
    def * = (vehiclecategoryid, name, description, maxcapacity) <> (VehiclecategoryRow.tupled, VehiclecategoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(vehiclecategoryid), Rep.Some(name), description, Rep.Some(maxcapacity)).shaped.<>({r=>import r._; _1.map(_=> VehiclecategoryRow.tupled((_1.get, _2.get, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column vehicleCategoryId SqlType(bigserial), AutoInc, PrimaryKey */
    val vehiclecategoryid: Rep[Long] = column[Long]("vehicleCategoryId", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(200,true) */
    val name: Rep[String] = column[String]("name", O.Length(200,varying=true))
    /** Database column description SqlType(varchar), Length(200,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(200,varying=true), O.Default(None))
    /** Database column maxCapacity SqlType(float8) */
    val maxcapacity: Rep[Double] = column[Double]("maxCapacity")
  }
  /** Collection-like TableQuery object for table Vehiclecategory */
  lazy val Vehiclecategory = new TableQuery(tag => new Vehiclecategory(tag))

  /** Entity class storing rows of table Vehicletype
   *  @param vehicletypeid Database column vehicleTypeId SqlType(bigserial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(200,true)
   *  @param description Database column description SqlType(varchar), Length(200,true), Default(None)
   *  @param vehiclecategoryid Database column vehicleCategoryId SqlType(int8) */
  case class VehicletypeRow(vehicletypeid: Long, name: String, description: Option[String] = None, vehiclecategoryid: Long)
  /** GetResult implicit for fetching VehicletypeRow objects using plain SQL queries */
  implicit def GetResultVehicletypeRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]]): GR[VehicletypeRow] = GR{
    prs => import prs._
    VehicletypeRow.tupled((<<[Long], <<[String], <<?[String], <<[Long]))
  }
  /** Table description of table VehicleType. Objects of this class serve as prototypes for rows in queries. */
  class Vehicletype(_tableTag: Tag) extends profile.api.Table[VehicletypeRow](_tableTag, Some("vehicle"), "VehicleType") {
    def * = (vehicletypeid, name, description, vehiclecategoryid) <> (VehicletypeRow.tupled, VehicletypeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(vehicletypeid), Rep.Some(name), description, Rep.Some(vehiclecategoryid)).shaped.<>({r=>import r._; _1.map(_=> VehicletypeRow.tupled((_1.get, _2.get, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column vehicleTypeId SqlType(bigserial), AutoInc, PrimaryKey */
    val vehicletypeid: Rep[Long] = column[Long]("vehicleTypeId", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(200,true) */
    val name: Rep[String] = column[String]("name", O.Length(200,varying=true))
    /** Database column description SqlType(varchar), Length(200,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(200,varying=true), O.Default(None))
    /** Database column vehicleCategoryId SqlType(int8) */
    val vehiclecategoryid: Rep[Long] = column[Long]("vehicleCategoryId")

    /** Foreign key referencing Vehiclecategory (database name VehicleType_vehicleCategoryId_fkey) */
    lazy val vehiclecategoryFk = foreignKey("VehicleType_vehicleCategoryId_fkey", vehiclecategoryid, Vehiclecategory)(r => r.vehiclecategoryid, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Vehicletype */
  lazy val Vehicletype = new TableQuery(tag => new Vehicletype(tag))
}
