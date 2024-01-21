using Microsoft.Extensions.Configuration.UserSecrets;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace GymTrackerAPI.DataTypes
{
    [Table("user")]
    public class User
    {
        [Key]
        [Required]
        [Column("user_id")]
        public Guid UserId { get; set; }

        [Required]
        [MaxLength(256)]
        [Column("first_name")]
        public string FirstName { get; set; }

        [Required]
        [MaxLength(256)]
        [Column("last_name")]
        public string LastName { get; set; }

        [Required]
        [Column("date_of_birth")]
        public DateTime DateOfBirth { get; set; }

        [Required]
        [Column("created_date")]
        public DateTime CreatedDate { get; set; }
    }
}
