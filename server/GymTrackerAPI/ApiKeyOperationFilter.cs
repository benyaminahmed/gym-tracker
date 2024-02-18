using Microsoft.OpenApi.Models;
using Swashbuckle.AspNetCore.SwaggerGen;

public class ApiKeyOperationFilter : IOperationFilter
{
    public void Apply(OpenApiOperation operation, OperationFilterContext context)
    {
        if (operation.Parameters == null)
            operation.Parameters = new List<OpenApiParameter>();

        operation.Parameters.Add(new OpenApiParameter
        {
            Name = "X-API-Key",
            In = ParameterLocation.Header,
            Description = "API Key",
            Required = true,
            Schema = new OpenApiSchema
            {
                Type = "String"
            }
        });
    }
}
