library(lattice)

data <<- numeric(100)

function(dataHolder) {
    svg()
    data <<- c(data[2:100], dataHolder$value)

    plot <- xyplot(data ~ seq(0, 99),
       main='CSV Data Plot',
       xlab="Index",
       ylab="Value",
       type = 'l',
       col = "#8B4513",
       panel = function(...) {
           panel.grid(h=-1, v=-1)
           panel.xyplot(...)
       })
    print(plot)
    svg.off()
}
